package net.minecraft.util;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.MoreExecutors;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.DSL.TypeReference;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.Hash.Strategy;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.time.Instant;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.client.util.ICharacterPredicate;
import net.minecraft.crash.ReportedException;
import net.minecraft.state.Property;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Bootstrap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Util {
   private static final AtomicInteger NEXT_SERVER_WORKER_ID = new AtomicInteger(1);
   private static final ExecutorService BOOTSTRAP_SERVICE = createNamedService("Bootstrap");
   private static final ExecutorService SERVER_EXECUTOR = createNamedService("Main");
   private static final ExecutorService RENDERING_SERVICE = startThreadedService();
   public static LongSupplier nanoTimeSupplier = System::nanoTime;
   public static final UUID DUMMY_UUID = new UUID(0L, 0L);
   private static final Logger LOGGER = LogManager.getLogger();

   public static <K, V> Collector<Entry<? extends K, ? extends V>, ?, Map<K, V>> toMapCollector() {
      return Collectors.toMap(Entry::getKey, Entry::getValue);
   }

   public static <T extends Comparable<T>> String getValueName(Property<T> property, Object value) {
      return property.getName((T)(value));
   }

   public static String makeTranslationKey(String type, @Nullable ResourceLocation id) {
      return id == null ? type + ".unregistered_sadface" : type + '.' + id.getNamespace() + '.' + id.getPath().replace('/', '.');
   }

   public static long milliTime() {
      return nanoTime() / 1000000L;
   }

   public static long nanoTime() {
      return nanoTimeSupplier.getAsLong();
   }

   public static long millisecondsSinceEpoch() {
      return Instant.now().toEpochMilli();
   }

   private static ExecutorService createNamedService(String serviceName) {
      int i = MathHelper.clamp(Runtime.getRuntime().availableProcessors() - 1, 1, 7);
      ExecutorService executorservice;
      if (i <= 0) {
         executorservice = MoreExecutors.newDirectExecutorService();
      } else {
         executorservice = new ForkJoinPool(i, (joinPool) -> {
            ForkJoinWorkerThread forkjoinworkerthread = new ForkJoinWorkerThread(joinPool) {
               protected void onTermination(Throwable p_onTermination_1_) {
                  if (p_onTermination_1_ != null) {
                     Util.LOGGER.warn("{} died", this.getName(), p_onTermination_1_);
                  } else {
                     Util.LOGGER.debug("{} shutdown", (Object)this.getName());
                  }

                  super.onTermination(p_onTermination_1_);
               }
            };
            forkjoinworkerthread.setName("Worker-" + serviceName + "-" + NEXT_SERVER_WORKER_ID.getAndIncrement());
            return forkjoinworkerthread;
         }, Util::printException, true);
      }

      return executorservice;
   }

   public static Executor getBootstrapService() {
      return BOOTSTRAP_SERVICE;
   }

   public static Executor getServerExecutor() {
      return SERVER_EXECUTOR;
   }

   public static Executor getRenderingService() {
      return RENDERING_SERVICE;
   }

   public static void shutdown() {
      shutdownService(SERVER_EXECUTOR);
      shutdownService(RENDERING_SERVICE);
   }

   private static void shutdownService(ExecutorService service) {
      service.shutdown();

      boolean flag;
      try {
         flag = service.awaitTermination(3L, TimeUnit.SECONDS);
      } catch (InterruptedException interruptedexception) {
         flag = false;
      }

      if (!flag) {
         service.shutdownNow();
      }

   }

   private static ExecutorService startThreadedService() {
      return Executors.newCachedThreadPool((runnable) -> {
         Thread thread = new Thread(runnable);
         thread.setName("IO-Worker-" + NEXT_SERVER_WORKER_ID.getAndIncrement());
         thread.setUncaughtExceptionHandler(Util::printException);
         return thread;
      });
   }

   @OnlyIn(Dist.CLIENT)
   public static <T> CompletableFuture<T> completedExceptionallyFuture(Throwable throwableIn) {
      CompletableFuture<T> completablefuture = new CompletableFuture<>();
      completablefuture.completeExceptionally(throwableIn);
      return completablefuture;
   }

   @OnlyIn(Dist.CLIENT)
   public static void toRuntimeException(Throwable throwableIn) {
      throw throwableIn instanceof RuntimeException ? (RuntimeException)throwableIn : new RuntimeException(throwableIn);
   }

   private static void printException(Thread thread, Throwable throwable) {
      pauseDevMode(throwable);
      if (throwable instanceof CompletionException) {
         throwable = throwable.getCause();
      }

      if (throwable instanceof ReportedException) {
         Bootstrap.printToSYSOUT(((ReportedException)throwable).getCrashReport().getCompleteReport());
         System.exit(-1);
      }

      LOGGER.error(String.format("Caught exception in thread %s", thread), throwable);
   }

   @Nullable
   public static Type<?> attemptDataFix(TypeReference type, String choiceName) {
      return !SharedConstants.useDatafixers ? null : attemptDataFixInternal(type, choiceName);
   }

   @Nullable
   private static Type<?> attemptDataFixInternal(TypeReference typeIn, String choiceName) {
      Type<?> type = null;

      try {
         type = DataFixesManager.getDataFixer().getSchema(DataFixUtils.makeKey(SharedConstants.getVersion().getWorldVersion())).getChoiceType(typeIn, choiceName);
      } catch (IllegalArgumentException illegalargumentexception) {
         LOGGER.debug("No data fixer registered for {}", (Object)choiceName);
         if (SharedConstants.developmentMode) {
            throw illegalargumentexception;
         }
      }

      return type;
   }

   public static Util.OS getOSType() {
      String s = System.getProperty("os.name").toLowerCase(Locale.ROOT);
      if (s.contains("win")) {
         return Util.OS.WINDOWS;
      } else if (s.contains("mac")) {
         return Util.OS.OSX;
      } else if (s.contains("solaris")) {
         return Util.OS.SOLARIS;
      } else if (s.contains("sunos")) {
         return Util.OS.SOLARIS;
      } else if (s.contains("linux")) {
         return Util.OS.LINUX;
      } else {
         return s.contains("unix") ? Util.OS.LINUX : Util.OS.UNKNOWN;
      }
   }

   public static Stream<String> getJvmFlags() {
      RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();
      return runtimemxbean.getInputArguments().stream().filter((argument) -> {
         return argument.startsWith("-X");
      });
   }

   public static <T> T getLast(List<T> listIn) {
      return listIn.get(listIn.size() - 1);
   }

   public static <T> T getElementAfter(Iterable<T> iterable, @Nullable T element) {
      Iterator<T> iterator = iterable.iterator();
      T t = iterator.next();
      if (element != null) {
         T t1 = t;

         while(t1 != element) {
            if (iterator.hasNext()) {
               t1 = iterator.next();
            }
         }

         if (iterator.hasNext()) {
            return iterator.next();
         }
      }

      return t;
   }

   public static <T> T getElementBefore(Iterable<T> iterable, @Nullable T current) {
      Iterator<T> iterator = iterable.iterator();

      T t;
      T t1;
      for(t = null; iterator.hasNext(); t = t1) {
         t1 = iterator.next();
         if (t1 == current) {
            if (t == null) {
               t = (T)(iterator.hasNext() ? Iterators.getLast(iterator) : current);
            }
            break;
         }
      }

      return t;
   }

   public static <T> T make(Supplier<T> supplier) {
      return supplier.get();
   }

   public static <T> T make(T object, Consumer<T> consumer) {
      consumer.accept(object);
      return object;
   }

   public static <K> Strategy<K> identityHashStrategy() {
      return (Strategy<K>)Util.IdentityStrategy.INSTANCE;
   }

   /**
    * Takes a list of futures and returns a future of list that completes when all of them succeed or any of them error,
    */
   public static <V> CompletableFuture<List<V>> gather(List<? extends CompletableFuture<? extends V>> futuresIn) {
      List<V> list = Lists.newArrayListWithCapacity(futuresIn.size());
      CompletableFuture<?>[] completablefuture = new CompletableFuture[futuresIn.size()];
      CompletableFuture<Void> completablefuture1 = new CompletableFuture<>();
      futuresIn.forEach((completable) -> {
         int i = list.size();
         list.add((V)null);
         completablefuture[i] = completable.whenComplete((result, throwable) -> {
            if (throwable != null) {
               completablefuture1.completeExceptionally(throwable);
            } else {
               list.set(i, result);
            }

         });
      });
      return CompletableFuture.allOf(completablefuture).applyToEither(completablefuture1, (nil) -> {
         return list;
      });
   }

   public static <T> Stream<T> streamOptional(Optional<? extends T> optionalIn) {
      return DataFixUtils.orElseGet(optionalIn.map(Stream::of), Stream::empty);
   }

   public static <T> Optional<T> acceptOrElse(Optional<T> opt, Consumer<T> consumer, Runnable orElse) {
      if (opt.isPresent()) {
         consumer.accept(opt.get());
      } else {
         orElse.run();
      }

      return opt;
   }

   public static Runnable namedRunnable(Runnable runnableIn, Supplier<String> supplierIn) {
      return runnableIn;
   }

   public static <T extends Throwable> T pauseDevMode(T throwableIn) {
      if (SharedConstants.developmentMode) {
         LOGGER.error("Trying to throw a fatal exception, pausing in IDE", throwableIn);

         while(true) {
            try {
               Thread.sleep(1000L);
               LOGGER.error("paused");
            } catch (InterruptedException interruptedexception) {
               return throwableIn;
            }
         }
      } else {
         return throwableIn;
      }
   }

   public static String getMessage(Throwable throwableIn) {
      if (throwableIn.getCause() != null) {
         return getMessage(throwableIn.getCause());
      } else {
         return throwableIn.getMessage() != null ? throwableIn.getMessage() : throwableIn.toString();
      }
   }

   public static <T> T getRandomObject(T[] selections, Random rand) {
      return selections[rand.nextInt(selections.length)];
   }

   public static int getRandomInt(int[] selections, Random rand) {
      return selections[rand.nextInt(selections.length)];
   }

   private static BooleanSupplier renameFile(final Path filePath, final Path newName) {
      return new BooleanSupplier() {
         public boolean getAsBoolean() {
            try {
               Files.move(filePath, newName);
               return true;
            } catch (IOException ioexception) {
               Util.LOGGER.error("Failed to rename", (Throwable)ioexception);
               return false;
            }
         }

         public String toString() {
            return "rename " + filePath + " to " + newName;
         }
      };
   }

   private static BooleanSupplier deleteFile(final Path filePath) {
      return new BooleanSupplier() {
         public boolean getAsBoolean() {
            try {
               Files.deleteIfExists(filePath);
               return true;
            } catch (IOException ioexception) {
               Util.LOGGER.warn("Failed to delete", (Throwable)ioexception);
               return false;
            }
         }

         public String toString() {
            return "delete old " + filePath;
         }
      };
   }

   private static BooleanSupplier verifyFileDeleted(final Path filePath) {
      return new BooleanSupplier() {
         public boolean getAsBoolean() {
            return !Files.exists(filePath);
         }

         public String toString() {
            return "verify that " + filePath + " is deleted";
         }
      };
   }

   private static BooleanSupplier verifyFileExists(final Path filePath) {
      return new BooleanSupplier() {
         public boolean getAsBoolean() {
            return Files.isRegularFile(filePath);
         }

         public String toString() {
            return "verify that " + filePath + " is present";
         }
      };
   }

   private static boolean verifyExecuted(BooleanSupplier... suppliers) {
      for(BooleanSupplier booleansupplier : suppliers) {
         if (!booleansupplier.getAsBoolean()) {
            LOGGER.warn("Failed to execute {}", (Object)booleansupplier);
            return false;
         }
      }

      return true;
   }

   private static boolean attemptExecution(int maxTries, String actionName, BooleanSupplier... suppliers) {
      for(int i = 0; i < maxTries; ++i) {
         if (verifyExecuted(suppliers)) {
            return true;
         }

         LOGGER.error("Failed to {}, retrying {}/{}", actionName, i, maxTries);
      }

      LOGGER.error("Failed to {}, aborting, progress might be lost", (Object)actionName);
      return false;
   }

   public static void backupThenUpdate(File current, File latest, File oldBackup) {
      backupThenUpdate(current.toPath(), latest.toPath(), oldBackup.toPath());
   }

   public static void backupThenUpdate(Path current, Path latest, Path oldBackup) {
      int i = 10;
      if (!Files.exists(current) || attemptExecution(10, "create backup " + oldBackup, deleteFile(oldBackup), renameFile(current, oldBackup), verifyFileExists(oldBackup))) {
         if (attemptExecution(10, "remove old " + current, deleteFile(current), verifyFileDeleted(current))) {
            if (!attemptExecution(10, "replace " + current + " with " + latest, renameFile(latest, current), verifyFileExists(current))) {
               attemptExecution(10, "restore " + current + " from " + oldBackup, renameFile(oldBackup, current), verifyFileExists(current));
            }

         }
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static int func_240980_a_(String text, int index, int direction) {
      int i = text.length();
      if (direction >= 0) {
         for(int j = 0; index < i && j < direction; ++j) {
            if (Character.isHighSurrogate(text.charAt(index++)) && index < i && Character.isLowSurrogate(text.charAt(index))) {
               ++index;
            }
         }
      } else {
         for(int k = direction; index > 0 && k < 0; ++k) {
            --index;
            if (Character.isLowSurrogate(text.charAt(index)) && index > 0 && Character.isHighSurrogate(text.charAt(index - 1))) {
               --index;
            }
         }
      }

      return index;
   }

   public static Consumer<String> prefixString(String prefix, Consumer<String> consumer) {
      return (merger) -> {
         consumer.accept(prefix + merger);
      };
   }

   public static DataResult<int[]> validateIntStreamSize(IntStream stream, int size) {
      int[] aint = stream.limit((long)(size + 1)).toArray();
      if (aint.length != size) {
         String s = "Input is not a list of " + size + " ints";
         return aint.length >= size ? DataResult.error(s, Arrays.copyOf(aint, size)) : DataResult.error(s);
      } else {
         return DataResult.success(aint);
      }
   }

   public static void setupTimerThread() {
      Thread thread = new Thread("Timer hack thread") {
         public void run() {
            while(true) {
               try {
                  Thread.sleep(2147483647L);
               } catch (InterruptedException interruptedexception) {
                  Util.LOGGER.warn("Timer hack thread interrupted, that really should not happen");
                  return;
               }
            }
         }
      };
      thread.setDaemon(true);
      thread.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER));
      thread.start();
   }

   @OnlyIn(Dist.CLIENT)
   public static void moveFileToDirectory(Path fromDirectory, Path toDirectory, Path filePath) throws IOException {
      Path path = fromDirectory.relativize(filePath);
      Path path1 = toDirectory.resolve(path);
      Files.copy(filePath, path1);
   }

   @OnlyIn(Dist.CLIENT)
   public static String validateCharacters(String fileName, ICharacterPredicate characterValidator) {
      return fileName.toLowerCase(Locale.ROOT).chars().mapToObj((character) -> {
         return characterValidator.test((char)character) ? Character.toString((char)character) : "_";
      }).collect(Collectors.joining());
   }

   static enum IdentityStrategy implements Strategy<Object> {
      INSTANCE;

      public int hashCode(Object p_hashCode_1_) {
         return System.identityHashCode(p_hashCode_1_);
      }

      public boolean equals(Object p_equals_1_, Object p_equals_2_) {
         return p_equals_1_ == p_equals_2_;
      }
   }

   public static enum OS {
      LINUX,
      SOLARIS,
      WINDOWS {
         @OnlyIn(Dist.CLIENT)
         protected String[] getOpenCommandLine(URL url) {
            return new String[]{"rundll32", "url.dll,FileProtocolHandler", url.toString()};
         }
      },
      OSX {
         @OnlyIn(Dist.CLIENT)
         protected String[] getOpenCommandLine(URL url) {
            return new String[]{"open", url.toString()};
         }
      },
      UNKNOWN;

      private OS() {
      }

      @OnlyIn(Dist.CLIENT)
      public void openURL(URL url) {
         try {
            Process process = AccessController.doPrivileged((PrivilegedExceptionAction<Process>)(() -> {
               return Runtime.getRuntime().exec(this.getOpenCommandLine(url));
            }));

            for(String s : IOUtils.readLines(process.getErrorStream())) {
               Util.LOGGER.error(s);
            }

            process.getInputStream().close();
            process.getErrorStream().close();
            process.getOutputStream().close();
         } catch (IOException | PrivilegedActionException privilegedactionexception) {
            Util.LOGGER.error("Couldn't open url '{}'", url, privilegedactionexception);
         }

      }

      @OnlyIn(Dist.CLIENT)
      public void openURI(URI uri) {
         try {
            this.openURL(uri.toURL());
         } catch (MalformedURLException malformedurlexception) {
            Util.LOGGER.error("Couldn't open uri '{}'", uri, malformedurlexception);
         }

      }

      @OnlyIn(Dist.CLIENT)
      public void openFile(File fileIn) {
         try {
            this.openURL(fileIn.toURI().toURL());
         } catch (MalformedURLException malformedurlexception) {
            Util.LOGGER.error("Couldn't open file '{}'", fileIn, malformedurlexception);
         }

      }

      @OnlyIn(Dist.CLIENT)
      protected String[] getOpenCommandLine(URL url) {
         String s = url.toString();
         if ("file".equals(url.getProtocol())) {
            s = s.replace("file:", "file://");
         }

         return new String[]{"xdg-open", s};
      }

      @OnlyIn(Dist.CLIENT)
      public void openURI(String uri) {
         try {
            this.openURL((new URI(uri)).toURL());
         } catch (MalformedURLException | IllegalArgumentException | URISyntaxException urisyntaxexception) {
            Util.LOGGER.error("Couldn't open uri '{}'", uri, urisyntaxexception);
         }

      }
   }
}

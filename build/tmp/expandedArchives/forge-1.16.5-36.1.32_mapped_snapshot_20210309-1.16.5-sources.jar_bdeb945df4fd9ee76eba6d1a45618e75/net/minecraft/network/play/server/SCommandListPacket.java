package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import javax.annotation.Nullable;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.command.arguments.SuggestionProviders;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;

public class SCommandListPacket implements IPacket<IClientPlayNetHandler> {
   private RootCommandNode<ISuggestionProvider> root;

   public SCommandListPacket() {
   }

   public SCommandListPacket(RootCommandNode<ISuggestionProvider> rootIn) {
      this.root = rootIn;
   }

   /**
    * Reads the raw packet data from the data stream.
    */
   public void readPacketData(PacketBuffer buf) throws IOException {
      SCommandListPacket.Entry[] ascommandlistpacket$entry = new SCommandListPacket.Entry[buf.readVarInt()];

      for(int i = 0; i < ascommandlistpacket$entry.length; ++i) {
         ascommandlistpacket$entry[i] = readEntry(buf);
      }

      sendCommandTree(ascommandlistpacket$entry);
      this.root = (RootCommandNode)ascommandlistpacket$entry[buf.readVarInt()].node;
   }

   /**
    * Writes the raw packet data to the data stream.
    */
   public void writePacketData(PacketBuffer buf) throws IOException {
      Object2IntMap<CommandNode<ISuggestionProvider>> object2intmap = getCommandNodeMap(this.root);
      CommandNode<ISuggestionProvider>[] commandnode = func_244293_a(object2intmap);
      buf.writeVarInt(commandnode.length);

      for(CommandNode<ISuggestionProvider> commandnode1 : commandnode) {
         writeCommandNode(buf, commandnode1, object2intmap);
      }

      buf.writeVarInt(object2intmap.get(this.root));
   }

   private static void sendCommandTree(SCommandListPacket.Entry[] entries) {
      List<SCommandListPacket.Entry> list = Lists.newArrayList(entries);

      while(!list.isEmpty()) {
         boolean flag = list.removeIf((entry) -> {
            return entry.createCommandNode(entries);
         });
         if (!flag) {
            throw new IllegalStateException("Server sent an impossible command tree");
         }
      }

   }

   private static Object2IntMap<CommandNode<ISuggestionProvider>> getCommandNodeMap(RootCommandNode<ISuggestionProvider> rootNode) {
      Object2IntMap<CommandNode<ISuggestionProvider>> object2intmap = new Object2IntOpenHashMap<>();
      Queue<CommandNode<ISuggestionProvider>> queue = Queues.newArrayDeque();
      queue.add(rootNode);

      CommandNode<ISuggestionProvider> commandnode;
      while((commandnode = queue.poll()) != null) {
         if (!object2intmap.containsKey(commandnode)) {
            int i = object2intmap.size();
            object2intmap.put(commandnode, i);
            queue.addAll(commandnode.getChildren());
            if (commandnode.getRedirect() != null) {
               queue.add(commandnode.getRedirect());
            }
         }
      }

      return object2intmap;
   }

   private static CommandNode<ISuggestionProvider>[] func_244293_a(Object2IntMap<CommandNode<ISuggestionProvider>> nodeMap) {
      CommandNode<ISuggestionProvider>[] commandnode = new CommandNode[nodeMap.size()];

      for(Object2IntMap.Entry<CommandNode<ISuggestionProvider>> entry : Object2IntMaps.fastIterable(nodeMap)) {
         commandnode[entry.getIntValue()] = entry.getKey();
      }

      return commandnode;
   }

   private static SCommandListPacket.Entry readEntry(PacketBuffer buffer) {
      byte b0 = buffer.readByte();
      int[] aint = buffer.readVarIntArray();
      int i = (b0 & 8) != 0 ? buffer.readVarInt() : 0;
      ArgumentBuilder<ISuggestionProvider, ?> argumentbuilder = readArgumentBuilder(buffer, b0);
      return new SCommandListPacket.Entry(argumentbuilder, b0, i, aint);
   }

   @Nullable
   private static ArgumentBuilder<ISuggestionProvider, ?> readArgumentBuilder(PacketBuffer buffer, byte buf) {
      int i = buf & 3;
      if (i == 2) {
         String s = buffer.readString(32767);
         ArgumentType<?> argumenttype = ArgumentTypes.deserialize(buffer);
         if (argumenttype == null) {
            return null;
         } else {
            RequiredArgumentBuilder<ISuggestionProvider, ?> requiredargumentbuilder = RequiredArgumentBuilder.argument(s, argumenttype);
            if ((buf & 16) != 0) {
               requiredargumentbuilder.suggests(SuggestionProviders.get(buffer.readResourceLocation()));
            }

            return requiredargumentbuilder;
         }
      } else {
         return i == 1 ? LiteralArgumentBuilder.literal(buffer.readString(32767)) : null;
      }
   }

   private static void writeCommandNode(PacketBuffer buffer, CommandNode<ISuggestionProvider> buf, Map<CommandNode<ISuggestionProvider>, Integer> node) {
      byte b0 = 0;
      if (buf.getRedirect() != null) {
         b0 = (byte)(b0 | 8);
      }

      if (buf.getCommand() != null) {
         b0 = (byte)(b0 | 4);
      }

      if (buf instanceof RootCommandNode) {
         b0 = (byte)(b0 | 0);
      } else if (buf instanceof ArgumentCommandNode) {
         b0 = (byte)(b0 | 2);
         if (((ArgumentCommandNode)buf).getCustomSuggestions() != null) {
            b0 = (byte)(b0 | 16);
         }
      } else {
         if (!(buf instanceof LiteralCommandNode)) {
            throw new UnsupportedOperationException("Unknown node type " + buf);
         }

         b0 = (byte)(b0 | 1);
      }

      buffer.writeByte(b0);
      buffer.writeVarInt(buf.getChildren().size());

      for(CommandNode<ISuggestionProvider> commandnode : buf.getChildren()) {
         buffer.writeVarInt(node.get(commandnode));
      }

      if (buf.getRedirect() != null) {
         buffer.writeVarInt(node.get(buf.getRedirect()));
      }

      if (buf instanceof ArgumentCommandNode) {
         ArgumentCommandNode<ISuggestionProvider, ?> argumentcommandnode = (ArgumentCommandNode)buf;
         buffer.writeString(argumentcommandnode.getName());
         ArgumentTypes.serialize(buffer, argumentcommandnode.getType());
         if (argumentcommandnode.getCustomSuggestions() != null) {
            buffer.writeResourceLocation(SuggestionProviders.getId(argumentcommandnode.getCustomSuggestions()));
         }
      } else if (buf instanceof LiteralCommandNode) {
         buffer.writeString(((LiteralCommandNode)buf).getLiteral());
      }

   }

   /**
    * Passes this Packet on to the NetHandler for processing.
    */
   public void processPacket(IClientPlayNetHandler handler) {
      handler.handleCommandList(this);
   }

   public RootCommandNode<ISuggestionProvider> getRoot() {
      return this.root;
   }

   static class Entry {
      @Nullable
      private final ArgumentBuilder<ISuggestionProvider, ?> argBuilder;
      private final byte flags;
      private final int redirectTarget;
      private final int[] children;
      @Nullable
      private CommandNode<ISuggestionProvider> node;

      private Entry(@Nullable ArgumentBuilder<ISuggestionProvider, ?> argBuilderIn, byte flagsIn, int redirectTargetIn, int[] childrenIn) {
         this.argBuilder = argBuilderIn;
         this.flags = flagsIn;
         this.redirectTarget = redirectTargetIn;
         this.children = childrenIn;
      }

      public boolean createCommandNode(SCommandListPacket.Entry[] nodeArray) {
         if (this.node == null) {
            if (this.argBuilder == null) {
               this.node = new RootCommandNode<>();
            } else {
               if ((this.flags & 8) != 0) {
                  if (nodeArray[this.redirectTarget].node == null) {
                     return false;
                  }

                  this.argBuilder.redirect(nodeArray[this.redirectTarget].node);
               }

               if ((this.flags & 4) != 0) {
                  this.argBuilder.executes((context) -> {
                     return 0;
                  });
               }

               this.node = this.argBuilder.build();
            }
         }

         for(int i : this.children) {
            if (nodeArray[i].node == null) {
               return false;
            }
         }

         for(int j : this.children) {
            CommandNode<ISuggestionProvider> commandnode = nodeArray[j].node;
            if (!(commandnode instanceof RootCommandNode)) {
               this.node.addChild(commandnode);
            }
         }

         return true;
      }
   }
}
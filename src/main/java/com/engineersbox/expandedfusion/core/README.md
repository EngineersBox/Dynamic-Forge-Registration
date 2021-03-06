# Dynamic Forge Registration

Developing forge mods comes with a lot of verbose, static, un-maintainable code.
Much of this is in the form of element registration for blocks, items, fluids, etc.

This core mod aims at removing all of that bloat from your mod and instead doing all
the registration dynamically for you. To avoid verbose inheritance overheads, this is
annotation driven and fully configurable for you to integrate with your preferred
code organization.

## Annotations

### Providers

| Annotation                    | Description                                                                                                                                                                                                                                                                                              | Primary |
|-------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
| `@BlockProvider`              | Declaration of a block extending from `net.minecraft.block.Block`                                                                                                                                                                                                                                        | `true`  |
| `@TileEntityProvider`         | Declaration of a tile entity from `net.minecraft.tileentity.TileEntity` associated with a `@BlockProvider` annotated block                                                                                                                                                                               | `true`  |
| `@ContainerProvider`          | Declaration of a container from `net.minecraft.inventory.container.Conatiner` associated with a `@BlockProvider` annotated block                                                                                                                                                                         | `true`  |
| `@ScreenProvider`             | Declaration of a container from `net.minecraft.client.gui.screen.Screen` associated with a `@BlockProvider` annotated block                                                                                                                                                                              | `true`  |
| `@RendererProvider`           | Declaration of a tile entity renderer from `net.minecraft.client.renderer.tileentity.TileEntityRenderer` associated with a `@BlockProvider` annotated block with annotation `type` field set to either `INTERACTIVE_TILE_ENTITY` (with optional usage) or `RENDERED_TILE_ENTITY` (with required usage)   | `true`  |
| `@BaseBlockProperties`        | Used in the `properties` field of a `@BlockProvider` annotation to dynamically specify block properties to pass to a suitable constructor.<br>`@BlockProvider` annotated class must be able to accept an instance of `net.minecraft.block.AbstractBlock.Properties`                                      | `false` |
| `@ItemProvider`               | Declaration of a container from `net.minecraft.client.gui.screen.Screen` associated with a `@BlockProvider` annotated block                                                                                                                                                                              | `true`  |
| `@FluidProvider`              | Declaration of a flowing or static fluid extending from `net.minecraftforge.fluid.ForgeFlowingFluid.Flowing` or `net.minecraftforge.fluid.ForgeFlowingFluid.static`                                                                                                                                      | `true`  |
| `@FluidBucketProperties`      | Used in the `bucket` field of a `@FluidProvider` annotation to provide item attributes for an automatically generated bucket item.                                                                                                                                                                       | `false` |
| `@AnonymousElementRegistrant` | Declaration of an anonymous element provider via enumeration or static fields                                                                                                                                                                                                                            | `true`  |
| `@ElementRetriever`           | Specifies a `field` or `method` that supplies an instance of `com.engineersbox.expandedfusion.core.registration.anonymous.element.AnonymousElement`. Usage with methods should only be applied where no parameters are specified. Can only be used in class annotated with `@AnonymousElementRegistrant` | `false` |
| `@FluidFog`                   | Provides RGB values for fluid fog to be set to when player is immersed. All values will default to `0.0F` if left unspecified.                                                                                                                                                                           | `false` |

### Events

| Annotation            | Description                                                                                                                                                                                                                                                                                                                                                    |
|-----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `@ClientEventHandler` | Marks a class implementing `EventSubscriptionHandler` to accept events from the client side event bus as instances of `ModLifecylceEvent`                                                                                                                                                                                                                      |
| `@ServerEventHandler` | Marks a class implementing `EventSubscriptionHandler` to accept events from the server side event bus as instances of `ServerLifecycleEvent`                                                                                                                                                                                                                   |
| `@CommonEventHandler` | Marks a class implementing `EventSubscriptionHandler` to accept events from either client or server side as instances of `ModLifecycleEvent`                                                                                                                                                                                                                   |
| `@DataEventHandler`   | Marks a class implementing `EventSubscriptionHandler` to accept events from either client or server side as instances of `GatherDataEvent`                                                                                                                                                                                                                     |
| `@Subscriber`         | Marks a method within a class annotated with any of  <ul><li>`@ClientEventHandler`</li><li>`@ServerEventHandler`</li><li>`@CommonEventHandler`</li><li>`@DataEventHandler`</li></ul>to subscribe to events published via the respective side. An annotated method should take a single parameter taking an instance of `net.minecraftforge.eventbus.api.Event` |

### Data

| Annotation      | Description                                                                                                                                                                                                                                                                                                 |
|-----------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `@LangMetadata` | Describes the human readable version of the registry element to generate a mappings for language files.<br>This should only be used on classes annotated with <ul><li>`@BlockProvider`</li><li>`@ContainerProvider`</li><li>`@ItemProvider`</li><li>`@FluidProvider`</li></ul>                              |
| `@LocaleEntry`  | Defines a mapping between an element of `com.engineersbox.expandedfusion.core.registration.handler.data.meta.lang.LangKey` and a human readable naming.                                                                                                                                            |
| `@Tag`          | Provides a tag resource location for a given registry element. This will create the tag if it does not exist, or add to if it does. <br>This should only be used on classes annotated with <ul><li>`@BlockProvider`</li><li>`@ContainerProvider`</li><li>`@ItemProvider`</li><li>`@FluidProvider`</li></ul> |

## Providers

TODO

### Blocks

TODO

#### Stateless Blocks

TODO

#### Tile Entities

TODO

### Items

TODO

### Fluids

TODO

#### Static

TODO

#### Flowing

TODO

### Recipes

TODO

### Tags

TODO

### Language Metadata

## Registration Process

### Overview

TODO

### JITRegistrationResolver

TODO

### Retrieving and Grouping Implementations

TODO

### Element Resolvers

TODO

#### Blocks

TODO

#### Items

TODO

#### Fluids

TODO

#### Recipe

TODO

#### Tags

TODO

### Events and Event Bus

TODO

## Annotation Processors

TODO

### Language Metadata

TODO

### Element Providers

TODO

### Recipes

TODO

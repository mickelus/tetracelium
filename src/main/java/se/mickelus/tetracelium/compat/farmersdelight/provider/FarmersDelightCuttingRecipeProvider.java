package se.mickelus.tetracelium.compat.farmersdelight.provider;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;
import se.mickelus.tetracelium.TetraceliumMod;
import se.mickelus.tetracelium.compat.farmersdelight.FarmersDelightToolActions;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.ingredient.ToolActionIngredient;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.ForgeTags;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.function.Consumer;

public class FarmersDelightCuttingRecipeProvider extends RecipeProvider {

    public FarmersDelightCuttingRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        Consumer<FinishedRecipe> consumerWrapper = recipe -> consumer.accept(new FinishedRecipeConditionWrapper(recipe));
        cuttingAnimalItems(consumerWrapper);
        cuttingVegetables(consumerWrapper);
        cuttingFoods(consumerWrapper);
        cuttingFlowers(consumerWrapper);
    }


    private static void cuttingAnimalItems(Consumer<FinishedRecipe> consumer) {

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BEEF), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.MINCED_BEEF.get(), 2)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.PORKCHOP), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.BACON.get(), 2)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.CHICKEN), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.CHICKEN_CUTS.get(), 2)
                .addResult(Items.BONE_MEAL)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.COOKED_CHICKEN), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.COOKED_CHICKEN_CUTS.get(), 2)
                .addResult(Items.BONE_MEAL)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.COD), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.COD_SLICE.get(), 2)
                .addResult(Items.BONE_MEAL)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.COOKED_COD), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.COOKED_COD_SLICE.get(), 2)
                .addResult(Items.BONE_MEAL)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.SALMON), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.SALMON_SLICE.get(), 2)
                .addResult(Items.BONE_MEAL)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.COOKED_SALMON), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.COOKED_SALMON_SLICE.get(), 2)
                .addResult(Items.BONE_MEAL)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.HAM.get()), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.PORKCHOP, 2)
                .addResult(Items.BONE)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.SMOKED_HAM.get()), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.COOKED_PORKCHOP, 2)
                .addResult(Items.BONE)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.MUTTON), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.MUTTON_CHOPS.get(), 2)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.COOKED_MUTTON), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.COOKED_MUTTON_CHOPS.get(), 2)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.INK_SAC), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.BLACK_DYE, 2)
                .build(consumer);
    }

    private static void cuttingVegetables(Consumer<FinishedRecipe> consumer) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.CABBAGE.get()), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.CABBAGE_LEAF.get(), 2)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.RICE_PANICLE.get()), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.RICE.get(), 1)
                .addResult(ModItems.STRAW.get())
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.MELON), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.MELON_SLICE, 9)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.PUMPKIN), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.PUMPKIN_SLICE.get(), 4)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.BROWN_MUSHROOM_COLONY.get()), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.BROWN_MUSHROOM, 5)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.RED_MUSHROOM_COLONY.get()), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.RED_MUSHROOM, 5)
                .build(consumer);
    }

    private static void cuttingFoods(Consumer<FinishedRecipe> consumer) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ForgeTags.DOUGH), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.RAW_PASTA.get(), 1)
                .build(consumer, new ResourceLocation(FarmersDelight.MODID, "cutting/tag_dough"));
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.KELP_ROLL.get()), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.KELP_ROLL_SLICE.get(), 3)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.CAKE), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.CAKE_SLICE.get(), 7)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.APPLE_PIE.get()), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.APPLE_PIE_SLICE.get(), 4)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.SWEET_BERRY_CHEESECAKE.get()), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get(), 4)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.CHOCOLATE_PIE.get()), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.CHOCOLATE_PIE_SLICE.get(), 4)
                .build(consumer);
    }

    private static void cuttingFlowers(Consumer<FinishedRecipe> consumer) {
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.WITHER_ROSE), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.BLACK_DYE, 2)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.CORNFLOWER), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.BLUE_DYE, 2)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.BLUE_ORCHID), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.LIGHT_BLUE_DYE, 2)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.AZURE_BLUET), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.LIGHT_GRAY_DYE, 2)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.OXEYE_DAISY), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.LIGHT_GRAY_DYE, 2)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.WHITE_TULIP), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.LIGHT_GRAY_DYE, 2)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.ALLIUM), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.MAGENTA_DYE, 2)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.ORANGE_TULIP), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.ORANGE_DYE, 2)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.PINK_TULIP), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.PINK_DYE, 2)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.RED_TULIP), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.RED_DYE, 2)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.POPPY), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.RED_DYE, 2)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.LILY_OF_THE_VALLEY), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.WHITE_DYE, 2)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(Items.DANDELION), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.YELLOW_DYE, 2)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_BEETROOTS.get()), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.BEETROOT_SEEDS, 1)
                .addResult(Items.RED_DYE)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_CABBAGES.get()), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.CABBAGE_SEEDS.get(), 1)
                .addResultWithChance(Items.YELLOW_DYE, 0.5F, 2)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_CARROTS.get()), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.CARROT, 1)
                .addResultWithChance(Items.LIGHT_GRAY_DYE, 0.5F, 2)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_ONIONS.get()), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.ONION.get(), 1)
                .addResult(Items.MAGENTA_DYE, 2)
                .addResultWithChance(Items.LIME_DYE, 0.1F)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_POTATOES.get()), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), Items.POTATO, 1)
                .addResultWithChance(Items.PURPLE_DYE, 0.5F, 2)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_RICE.get()), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.RICE.get(), 1)
                .addResultWithChance(ModItems.STRAW.get(), 0.5F)
                .build(consumer);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ModItems.WILD_TOMATOES.get()), new ToolActionIngredient(FarmersDelightToolActions.bladeCut), ModItems.TOMATO_SEEDS.get(), 1)
                .addResultWithChance(ModItems.TOMATO.get(), 0.2F)
                .addResultWithChance(Items.GREEN_DYE, 0.1F)
                .build(consumer);
    }

    static class FinishedRecipeConditionWrapper implements FinishedRecipe {
        FinishedRecipe recipe;

        public FinishedRecipeConditionWrapper(FinishedRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            recipe.serializeRecipeData(json);

            JsonObject condition = new JsonObject();
            condition.addProperty("type", "forge:mod_loaded");
            condition.addProperty("modid", "farmersdelight");

            JsonArray conditions = new JsonArray();
            conditions.add(condition);

            json.add("conditions", conditions);
        }

        @Override
        public ResourceLocation getId() {
            return new ResourceLocation(TetraceliumMod.MOD_ID, recipe.getId().getPath());
        }

        @Override
        public RecipeSerializer<?> getType() {
            return recipe.getType();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return recipe.serializeAdvancement();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return recipe.getAdvancementId();
        }
    }
}

package io.github.anjoismysign.blobcontainer.entity;

import io.github.anjoismysign.bloblib.utilities.ItemStackUtil;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SerialContainer {

    public static String serialize(@Nullable Map<Integer, @Nullable ItemStack> contents) {
        if (contents == null){
            return "";
        }
        int maxIndex = contents.keySet().stream()
                .max(Integer::compare)
                .orElse(-1);
        ItemStack[] array = new ItemStack[maxIndex + 1];
        contents.forEach((index, itemStack) -> {
            if (index >= 0) {
                array[index] = itemStack;
            }
        });
        return ItemStackUtil.itemStackArrayToBase64(array);
    }

    public static Map<Integer, @Nullable ItemStack> deserialize(@Nullable String base64) {
        @Nullable ItemStack[] array = base64 == null || base64.isEmpty() ? null : ItemStackUtil.itemStackArrayFromBase64(base64);
        Map<Integer, @Nullable ItemStack> contents = new HashMap<>();
        if (array == null) {
            return contents;
        }
        for (int i = 0; i < array.length; i++) {
            contents.put(i, array[i]);
        }
        return contents;
    }

}

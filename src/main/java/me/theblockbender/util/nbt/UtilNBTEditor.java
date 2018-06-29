package me.theblockbender.util.nbt;

import com.google.common.primitives.Primitives;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author BananaPuncher714
 * https://www.spigotmc.org/members/bananapuncher714.53668/
 * @version 5.0
 * <p>
 * Simple tutorial:
 * Setter: ItemStack item = NBTEditor.setItemTag( item, "value", "key");
 * Getter: Object value = NBTEditor.getItemTag( item, "key" );
 */
public class UtilNBTEditor {
    private static HashMap<String, Class<?>> classCache;
    private static HashMap<String, Method> methodCache;
    private static HashMap<Class<?>, Constructor<?>> constructorCache;
    private static HashMap<Class<?>, Class<?>> NBTClasses;
    private static HashMap<Class<?>, Field> NBTTagFieldCache;
    private static Field NBTListData;
    private static Field NBTCompoundMap;
    private static String version;

    static {
        version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        classCache = new HashMap<>();
        try {
            classCache.put("NBTTagCompound", Class.forName("net.minecraft.server." + version + "." + "NBTTagCompound"));
            classCache.put("NBTTagList", Class.forName("net.minecraft.server." + version + "." + "NBTTagList"));
            classCache.put("NBTBase", Class.forName("net.minecraft.server." + version + "." + "NBTBase"));

            classCache.put("ItemStack", Class.forName("net.minecraft.server." + version + "." + "ItemStack"));
            classCache.put("CraftItemStack", Class.forName("org.bukkit.craftbukkit." + version + ".inventory." + "CraftItemStack"));

            classCache.put("Entity", Class.forName("net.minecraft.server." + version + "." + "Entity"));
            classCache.put("CraftEntity", Class.forName("org.bukkit.craftbukkit." + version + ".entity." + "CraftEntity"));
            classCache.put("EntityLiving", Class.forName("net.minecraft.server." + version + "." + "EntityLiving"));

            classCache.put("CraftWorld", Class.forName("org.bukkit.craftbukkit." + version + "." + "CraftWorld"));
            classCache.put("CraftBlockState", Class.forName("org.bukkit.craftbukkit." + version + ".block." + "CraftBlockState"));
            classCache.put("BlockPosition", Class.forName("net.minecraft.server." + version + "." + "BlockPosition"));
            classCache.put("TileEntity", Class.forName("net.minecraft.server." + version + "." + "TileEntity"));
            classCache.put("World", Class.forName("net.minecraft.server." + version + "." + "World"));

            classCache.put("TileEntitySkull", Class.forName("net.minecraft.server." + version + "." + "TileEntitySkull"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        NBTClasses = new HashMap<>();
        try {
            NBTClasses.put(Byte.class, Class.forName("net.minecraft.server." + version + "." + "NBTTagByte"));
            NBTClasses.put(String.class, Class.forName("net.minecraft.server." + version + "." + "NBTTagString"));
            NBTClasses.put(Double.class, Class.forName("net.minecraft.server." + version + "." + "NBTTagDouble"));
            NBTClasses.put(Integer.class, Class.forName("net.minecraft.server." + version + "." + "NBTTagInt"));
            NBTClasses.put(Long.class, Class.forName("net.minecraft.server." + version + "." + "NBTTagLong"));
            NBTClasses.put(Short.class, Class.forName("net.minecraft.server." + version + "." + "NBTTagShort"));
            NBTClasses.put(Float.class, Class.forName("net.minecraft.server." + version + "." + "NBTTagFloat"));
            NBTClasses.put(Class.forName("[B"), Class.forName("net.minecraft.server." + version + "." + "NBTTagByteArray"));
            NBTClasses.put(Class.forName("[I"), Class.forName("net.minecraft.server." + version + "." + "NBTTagIntArray"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        methodCache = new HashMap<>();
        try {
            methodCache.put("get", Objects.requireNonNull(getNMSClass("NBTTagCompound")).getMethod("get", String.class));
            methodCache.put("set", Objects.requireNonNull(getNMSClass("NBTTagCompound")).getMethod("set", String.class, getNMSClass("NBTBase")));
            methodCache.put("hasKey", Objects.requireNonNull(getNMSClass("NBTTagCompound")).getMethod("hasKey", String.class));
            methodCache.put("setIndex", Objects.requireNonNull(getNMSClass("NBTTagList")).getMethod("a", int.class, getNMSClass("NBTBase")));
            methodCache.put("add", Objects.requireNonNull(getNMSClass("NBTTagList")).getMethod("add", getNMSClass("NBTBase")));

            methodCache.put("hasTag", Objects.requireNonNull(getNMSClass("ItemStack")).getMethod("hasTag"));
            methodCache.put("getTag", Objects.requireNonNull(getNMSClass("ItemStack")).getMethod("getTag"));
            methodCache.put("setTag", Objects.requireNonNull(getNMSClass("ItemStack")).getMethod("setTag", getNMSClass("NBTTagCompound")));
            methodCache.put("asNMSCopy", Objects.requireNonNull(getNMSClass("CraftItemStack")).getMethod("asNMSCopy", ItemStack.class));
            methodCache.put("asBukkitCopy", Objects.requireNonNull(getNMSClass("CraftItemStack")).getMethod("asBukkitCopy", getNMSClass("ItemStack")));

            methodCache.put("getEntityHandle", Objects.requireNonNull(getNMSClass("CraftEntity")).getMethod("getHandle"));
            methodCache.put("getEntityTag", Objects.requireNonNull(getNMSClass("Entity")).getMethod("c", getNMSClass("NBTTagCompound")));
            methodCache.put("setEntityTag", Objects.requireNonNull(getNMSClass("Entity")).getMethod("f", getNMSClass("NBTTagCompound")));

            if (version.contains("1_12")) {
                methodCache.put("setTileTag", Objects.requireNonNull(getNMSClass("TileEntity")).getMethod("load", getNMSClass("NBTTagCompound")));
            } else {
                methodCache.put("setTileTag", Objects.requireNonNull(getNMSClass("TileEntity")).getMethod("a", getNMSClass("NBTTagCompound")));
            }
            methodCache.put("getTileEntity", Objects.requireNonNull(getNMSClass("World")).getMethod("getTileEntity", getNMSClass("BlockPosition")));
            methodCache.put("getWorldHandle", Objects.requireNonNull(getNMSClass("CraftWorld")).getMethod("getHandle"));

            methodCache.put("setGameProfile", Objects.requireNonNull(getNMSClass("TileEntitySkull")).getMethod("setGameProfile", GameProfile.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            methodCache.put("getTileTag", Objects.requireNonNull(getNMSClass("TileEntity")).getMethod("save", getNMSClass("NBTTagCompound")));
        } catch (NoSuchMethodException exception) {
            try {
                methodCache.put("getTileTag", Objects.requireNonNull(getNMSClass("TileEntity")).getMethod("b", getNMSClass("NBTTagCompound")));
            } catch (Exception exception2) {
                exception2.printStackTrace();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        constructorCache = new HashMap<>();
        try {
            constructorCache.put(getNBTTag(Byte.class), getNBTTag(Byte.class).getConstructor(byte.class));
            constructorCache.put(getNBTTag(String.class), getNBTTag(String.class).getConstructor(String.class));
            constructorCache.put(getNBTTag(Double.class), getNBTTag(Double.class).getConstructor(double.class));
            constructorCache.put(getNBTTag(Integer.class), getNBTTag(Integer.class).getConstructor(int.class));
            constructorCache.put(getNBTTag(Long.class), getNBTTag(Long.class).getConstructor(long.class));
            constructorCache.put(getNBTTag(Float.class), getNBTTag(Float.class).getConstructor(float.class));
            constructorCache.put(getNBTTag(Short.class), getNBTTag(Short.class).getConstructor(short.class));
            constructorCache.put(getNBTTag(Class.forName("[B")), getNBTTag(Class.forName("[B")).getConstructor(Class.forName("[B")));
            constructorCache.put(getNBTTag(Class.forName("[I")), getNBTTag(Class.forName("[I")).getConstructor(Class.forName("[I")));

            constructorCache.put(getNMSClass("BlockPosition"), Objects.requireNonNull(getNMSClass("BlockPosition")).getConstructor(int.class, int.class, int.class));
        } catch (Exception e) {
            e.printStackTrace();
        }

        NBTTagFieldCache = new HashMap<>();
        try {
            for (Class<?> clazz : NBTClasses.values()) {
                Field data = clazz.getDeclaredField("data");
                data.setAccessible(true);
                NBTTagFieldCache.put(clazz, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            NBTListData = Objects.requireNonNull(getNMSClass("NBTTagList")).getDeclaredField("list");
            NBTListData.setAccessible(true);
            NBTCompoundMap = Objects.requireNonNull(getNMSClass("NBTTagCompound")).getDeclaredField("map");
            NBTCompoundMap.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getPrimitiveClass(Class<?> clazz) {
        return Primitives.unwrap(clazz);
    }

    public static Class<?> getNBTTag(Class<?> primitiveType) {
        if (NBTClasses.containsKey(primitiveType))
            return NBTClasses.get(primitiveType);
        return primitiveType;
    }

    public static Object getNBTVar(Object object) {
        if (object == null) return null;
        Class<?> clazz = object.getClass();
        try {
            if (NBTTagFieldCache.containsKey(clazz)) {
                return NBTTagFieldCache.get(clazz).get(object);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(String name) {
        return methodCache.getOrDefault(name, null);
    }

    public static Constructor<?> getConstructor(Class<?> clazz) {
        return constructorCache.getOrDefault(clazz, null);
    }

    public static Class<?> getNMSClass(String name) {
        if (classCache.containsKey(name)) {
            return classCache.get(name);
        }

        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMatch(String string, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    public static ItemStack getHead(String skinURL) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        if (skinURL == null || skinURL.isEmpty()) {
            return head;
        }
        ItemMeta headMeta = head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{\"url\":\"%s\"}}}", skinURL).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        assert profileField != null;
        profileField.setAccessible(true);
        try {
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    public static String getTexture(ItemStack head) {
        ItemMeta meta = head.getItemMeta();
        Field profileField = null;
        try {
            profileField = meta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        assert profileField != null;
        profileField.setAccessible(true);
        try {
            GameProfile profile = (GameProfile) profileField.get(meta);
            if (profile == null) {
                return null;
            }

            for (Property prop : profile.getProperties().values()) {
                if (prop.getName().equals("textures")) {
                    String texture = new String(Base64.decodeBase64(prop.getValue()));
                    return getMatch(texture, "\\{\"url\":\"(.*?)\"\\}");
                }
            }
            return null;
        } catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets an NBT tag in a given item with the specified keys
     *
     * @param item The itemstack to get the keys from
     * @param keys The keys to fetch; an integer after a key value indicates that it should get the nth place of
     *             the previous compound because it is a list;
     * @return The item represented by the keys, and an integer if it is showing how long a list is.
     */
    public static Object getItemTag(ItemStack item, Object... keys) {
        try {
            Object stack;
            stack = getMethod("asNMSCopy").invoke(null, item);

            Object tag;

            if (getMethod("hasTag").invoke(stack).equals(true)) {
                tag = getMethod("getTag").invoke(stack);
            } else {
                tag = Objects.requireNonNull(getNMSClass("NBTTagCompound")).newInstance();
            }

            return getTag(tag, keys);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Sets an NBT tag in an item with the provided keys and value
     *
     * @param item  The itemstack to set
     * @param keys  The keys to set, String for NBTCompound, int or null for an NBTTagList
     * @param value The value to set
     * @return A new ItemStack with the updated NBT tags
     */
    public static ItemStack setItemTag(ItemStack item, Object value, Object... keys) {
        try {
            Object stack = getMethod("asNMSCopy").invoke(null, item);

            Object tag;

            if (getMethod("hasTag").invoke(stack).equals(true)) {
                tag = getMethod("getTag").invoke(stack);
            } else {
                tag = Objects.requireNonNull(getNMSClass("NBTTagCompound")).newInstance();
            }

            setTag(tag, value, keys);
            getMethod("setTag").invoke(stack, tag);
            return (ItemStack) getMethod("asBukkitCopy").invoke(null, stack);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Gets an NBT tag in a given entity with the specified keys
     *
     * @param entity The entity to get the keys from
     * @param keys   The keys to fetch; an integer after a key value indicates that it should get the nth place of
     *               the previous compound because it is a list;
     * @return The item represented by the keys, and an integer if it is showing how long a list is.
     */
    public static Object getEntityTag(Entity entity, Object... keys) {
        try {
            Object NMSEntity = getMethod("getEntityHandle").invoke(entity);

            Object tag = Objects.requireNonNull(getNMSClass("NBTTagCompound")).newInstance();

            getMethod("getEntityTag").invoke(NMSEntity, tag);

            return getTag(tag, keys);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Sets an NBT tag in an entity with the provided keys and value
     *
     * @param entity The entity to set
     * @param keys   The keys to set, String for NBTCompound, int or null for an NBTTagList
     * @param value  The value to set
     */
    public static void setEntityTag(Entity entity, Object value, Object... keys) {
        try {
            Object NMSEntity = getMethod("getEntityHandle").invoke(entity);

            Object tag = Objects.requireNonNull(getNMSClass("NBTTagCompound")).newInstance();

            getMethod("getEntityTag").invoke(NMSEntity, tag);

            setTag(tag, value, keys);

            getMethod("setEntityTag").invoke(NMSEntity, tag);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Gets an NBT tag in a given block with the specified keys
     *
     * @param block The block to get the keys from
     * @param keys  The keys to fetch; an integer after a key value indicates that it should get the nth place of
     *              the previous compound because it is a list;
     * @return The item represented by the keys, and an integer if it is showing how long a list is.
     */
    public static Object getBlockTag(Block block, Object... keys) {
        try {
            if (!Objects.requireNonNull(getNMSClass("CraftBlockState")).isInstance(block.getState())) {
                return null;
            }

            Object tileEntity = getMethod("getTileEntity").invoke(block.getState());

            Object tag = getMethod("getTileTag").invoke(tileEntity, Objects.requireNonNull(getNMSClass("NBTTagCompound")).newInstance());

            return getTag(tag, keys);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Sets an NBT tag in an block with the provided keys and value
     *
     * @param block The block to set
     * @param keys  The keys to set, String for NBTCompound, int or null for an NBTTagList
     * @param value The value to set
     */
    public static void setBlockTag(Block block, Object value, Object... keys) {
        try {
            Location location = block.getLocation();

            Object blockPosition = getConstructor(getNMSClass("BlockPosition")).newInstance(location.getBlockX(), location.getBlockY(), location.getBlockZ());

            Object nmsWorld = getMethod("getWorldHandle").invoke(location.getWorld());

            Object tileEntity = getMethod("getTileEntity").invoke(nmsWorld, blockPosition);

            Object tag = getMethod("getTileTag").invoke(tileEntity, Objects.requireNonNull(getNMSClass("NBTTagCompound")).newInstance());

            setTag(tag, value, keys);

            getMethod("setTileTag").invoke(tileEntity, tag);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void setSkullTexture(Block block, String texture) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new com.mojang.authlib.properties.Property("textures", new String(Base64.encodeBase64(String.format("{textures:{SKIN:{\"url\":\"%s\"}}}", texture).getBytes()))));

        try {
            Location location = block.getLocation();

            Object blockPosition = getConstructor(getNMSClass("BlockPosition")).newInstance(location.getBlockX(), location.getBlockY(), location.getBlockZ());

            Object nmsWorld = getMethod("getWorldHandle").invoke(location.getWorld());

            Object tileEntity = getMethod("getTileEntity").invoke(nmsWorld, blockPosition);

            getMethod("setGameProfile").invoke(tileEntity, profile);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static void setTag(Object tag, Object value, Object... keys) throws Exception {
        Object notCompound = getConstructor(getNBTTag(value.getClass())).newInstance(value);

        Object compound = tag;
        for (int index = 0; index < keys.length; index++) {
            Object key = keys[index];
            if (index + 1 == keys.length) {
                if (key == null) {
                    getMethod("add").invoke(compound, notCompound);
                } else if (key instanceof Integer) {
                    getMethod("setIndex").invoke(compound, key, notCompound);
                } else {
                    getMethod("set").invoke(compound, key, notCompound);
                }
                break;
            }
            Object oldCompound = compound;
            if (key instanceof Integer) {
                compound = ((List<?>) NBTListData.get(compound)).get((int) key);
            } else if (key != null) {
                compound = getMethod("get").invoke(compound, (String) key);
            }
            if (compound == null || key == null) {
                if (keys[index + 1] == null || keys[index + 1] instanceof Integer) {
                    compound = Objects.requireNonNull(getNMSClass("NBTTagList")).newInstance();
                } else {
                    compound = Objects.requireNonNull(getNMSClass("NBTTagCompound")).newInstance();
                }
                if (oldCompound.getClass().getSimpleName().equals("NBTTagList")) {
                    getMethod("add").invoke(oldCompound, compound);
                } else {
                    getMethod("set").invoke(oldCompound, key, compound);
                }
            }
        }
    }

    private static Object getTag(Object tag, Object... keys) throws Exception {
        if (keys.length == 0) return getTags(tag);

        Object notCompound = tag;

        for (Object key : keys) {
            if (notCompound == null) return null;
            if (Objects.requireNonNull(getNMSClass("NBTTagCompound")).isInstance(notCompound)) {
                notCompound = getMethod("get").invoke(notCompound, (String) key);
            } else if (Objects.requireNonNull(getNMSClass("NBTTagList")).isInstance(notCompound)) {
                notCompound = ((List<?>) NBTListData.get(notCompound)).get((int) key);
            } else {
                return getNBTVar(notCompound);
            }
        }
        if (notCompound == null) return null;
        if (Objects.requireNonNull(getNMSClass("NBTTagList")).isInstance(notCompound)) {
            return getTags(notCompound);
        } else if (Objects.requireNonNull(getNMSClass("NBTTagCompound")).isInstance(notCompound)) {
            return getTags(notCompound);
        } else {
            return getNBTVar(notCompound);
        }
    }

    private static Object getTags(Object tag) {
        HashMap<Object, Object> tags = new HashMap<>();
        try {
            if (Objects.requireNonNull(getNMSClass("NBTTagCompound")).isInstance(tag)) {
                Map<String, Object> tagCompound = (Map<String, Object>) NBTCompoundMap.get(tag);
                for (String key : tagCompound.keySet()) {
                    Object value = tagCompound.get(key);
                    if (Objects.requireNonNull(getNMSClass("NBTTagEnd")).isInstance(value)) continue;
                    tags.put(key, getTag(value));
                }
            } else if (Objects.requireNonNull(getNMSClass("NBTTagList")).isInstance(tag)) {
                List<Object> tagList = (List<Object>) NBTListData.get(tag);
                for (int index = 0; index < tagList.size(); index++) {
                    Object value = tagList.get(index);
                    if (Objects.requireNonNull(getNMSClass("NBTTagEnd")).isInstance(value)) continue;
                    tags.put(index, getTag(value));
                }
            } else {
                return getNBTVar(tag);
            }
            return tags;
        } catch (Exception e) {
            e.printStackTrace();
            return tags;
        }
    }
}
package com.adrianwowk.hypixel.client.options;

import com.google.common.base.Splitter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class HypixelOptions {
    private final File optionsFile;
    public String API_KEY;
    public static HypixelOptions instance;
    private static final Logger LOGGER = LogManager.getLogger();
    public static final Splitter COLON_SPLITTER = Splitter.on(':');
    public MinecraftClient client;

    public HypixelOptions(MinecraftClient client, File optionsFile){
        this.optionsFile = new File(optionsFile, "plancke_options.txt");
        this.client = client;
//        this.load();
    }

//    public void load() {
//        try {
//            if (!this.optionsFile.exists()) {
//                return;
//            }
//
//            this.soundVolumeLevels.clear();
//            List<String> list = IOUtils.readLines((InputStream)(new FileInputStream(this.optionsFile)));
//            CompoundTag compoundTag = new CompoundTag();
//            Iterator var3 = list.iterator();
//
//            String string2;
//            while(var3.hasNext()) {
//                string2 = (String)var3.next();
//
//                try {
//                    Iterator<String> iterator = COLON_SPLITTER.omitEmptyStrings().limit(2).split(string2).iterator();
//                    compoundTag.putString((String)iterator.next(), (String)iterator.next());
//                } catch (Exception var10) {
//                    LOGGER.warn((String)"Skipping bad option: {}", (Object)string2);
//                }
//            }
//
//            compoundTag = this.method_1626(compoundTag);
//            var3 = compoundTag.getKeys().iterator();
//
//            while(var3.hasNext()) {
//                string2 = (String)var3.next();
//                String string3 = compoundTag.getString(string2);
//
//                try {
//                    if ("autoJump".equals(string2)) {
//                        Option.AUTO_JUMP.set(this, string3);
//                    }
//
//                    if ("autoSuggestions".equals(string2)) {
//                        Option.AUTO_SUGGESTIONS.set(this, string3);
//                    }
//
//                } catch (Exception var11) {
//                    LOGGER.warn((String)"Skipping bad option: {}:{}", (Object)string2, (Object)string3);
//                }
//            }
//
//            KeyBinding.updateKeysByCode();
//        } catch (Exception var12) {
//            LOGGER.error((String)"Failed to load options", (Throwable)var12);
//        }
//    }

    private CompoundTag method_1626(CompoundTag compoundTag) {
        int i = 0;

        try {
            i = Integer.parseInt(compoundTag.getString("version"));
        } catch (RuntimeException var4) {
        }

        return NbtHelper.update(this.client.getDataFixer(), DataFixTypes.OPTIONS, compoundTag, i);
    }
}

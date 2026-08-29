package com.ninni.species;

import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.UUID;

public class SpeciesDevelopers {

    public static Map<UUID, SpeciesDeveloperNames> DEVELOPER_UUIDS;

    public static void setDeveloperUuids() {
        DEVELOPER_UUIDS = Map.ofEntries(
            Map.entry(UUID.fromString("2d173722-de6b-4bb8-b21b-b2843cfe395d"), SpeciesDevelopers.SpeciesDeveloperNames.NINNI),
            Map.entry(UUID.fromString("f1fb25f4-60c4-4e21-b33c-59f0a2daf4b1"), SpeciesDevelopers.SpeciesDeveloperNames.REDA),
            Map.entry(UUID.fromString("4a463319-625c-4b86-a4e7-8b700f023a60"), SpeciesDevelopers.SpeciesDeveloperNames.NOON),
            Map.entry(UUID.fromString("603d30f1-77a1-4b88-b8c5-624a02feabcc"), SpeciesDevelopers.SpeciesDeveloperNames.BORNULHU),
//        Map.entry(UUID.fromString(""), SpeciesDevelopers.SpeciesDeveloperNames.GLADOS), // Glados does not own minecraft
            Map.entry(UUID.fromString("3ff497ed-71f0-453a-a869-4fc17be298ff"), SpeciesDevelopers.SpeciesDeveloperNames.YAPETTO),
            Map.entry(UUID.fromString("81499a26-ba39-430e-8009-29ee87351c20"), SpeciesDevelopers.SpeciesDeveloperNames.ORCINUS),
            Map.entry(UUID.fromString("0c22615f-a189-4f4e-85ae-79fd80c353c8"), SpeciesDevelopers.SpeciesDeveloperNames.VAKY),
            Map.entry(UUID.fromString("aca529a2-1166-41aa-b304-209f06831998"), SpeciesDevelopers.SpeciesDeveloperNames.TAZZ),
            Map.entry(UUID.fromString("f6dffbc0-746a-41fe-b0c1-20f9a596795a"), SpeciesDevelopers.SpeciesDeveloperNames.BUNTEN),
            Map.entry(UUID.fromString("4f00e7fc-b325-4f16-88cf-80cd78733646"), SpeciesDevelopers.SpeciesDeveloperNames.EXCLAIM)
        );
    }

    public static final ResourceLocation NINNI_TEXTURE = Species.of("textures/entity/capes/ninni.png");
    public static final ResourceLocation REDA_TEXTURE = Species.of("textures/entity/capes/floofhips.png");
    public static final ResourceLocation NOON_TEXTURE = Species.of("textures/entity/capes/noonyeyz.png");
    public static final ResourceLocation BORNULHU_TEXTURE = Species.of("textures/entity/capes/bornulhu.png");
    public static final ResourceLocation GLADOS_TEXTURE = Species.of("textures/entity/capes/glados_edition.png");
    public static final ResourceLocation YAPETTO_TEXTURE = Species.of("textures/entity/capes/yapetto.png");
    public static final ResourceLocation CONTRIBUTOR_TEXTURE = Species.of("textures/entity/capes/contributor.png");

    public enum SpeciesDeveloperNames {
        NINNI("Ninni", ContributionLevel.DEVELOPER, ChatFormatting.DARK_AQUA, NINNI_TEXTURE),
        REDA("Floofhips", ContributionLevel.DEVELOPER, ChatFormatting.RED, REDA_TEXTURE),
        NOON("Noonyeyz", ContributionLevel.DEVELOPER, ChatFormatting.AQUA, NOON_TEXTURE),
        BORNULHU("Bornulhu", ContributionLevel.DEVELOPER, ChatFormatting.GREEN, BORNULHU_TEXTURE),
        GLADOS("GLaDOS edition", ContributionLevel.DEVELOPER, ChatFormatting.LIGHT_PURPLE, GLADOS_TEXTURE),
        YAPETTO("Yapetto", ContributionLevel.GUEST_ARTIST, ChatFormatting.BLUE, YAPETTO_TEXTURE),
        ORCINUS("Orcinus", ContributionLevel.CONTRIBUTOR, ChatFormatting.BLUE, CONTRIBUTOR_TEXTURE),
        VAKY("VakyPanda", ContributionLevel.CONTRIBUTOR, ChatFormatting.BLUE, CONTRIBUTOR_TEXTURE),
        TAZZ("Tazz", ContributionLevel.CONTRIBUTOR, ChatFormatting.BLUE, CONTRIBUTOR_TEXTURE),
        BUNTEN("lunarbunten", ContributionLevel.COMPOSER, ChatFormatting.BLUE, CONTRIBUTOR_TEXTURE),
        EXCLAIM("Exclaim!", ContributionLevel.COMPOSER, ChatFormatting.BLUE, CONTRIBUTOR_TEXTURE);

        private final String name;
        private final ContributionLevel contributionLevel;
        private final ChatFormatting formatting;
        private final ResourceLocation capeTexture;

        SpeciesDeveloperNames(String name, ContributionLevel contributionLevel, ChatFormatting formatting, ResourceLocation capeTexture) {
            this.name = name;
            this.contributionLevel = contributionLevel;
            this.formatting = formatting;
            this.capeTexture = capeTexture;
        }

        public String getName() {
            return name;
        }
        public ContributionLevel getContributionLevel() {
            return contributionLevel;
        }
        public ChatFormatting getFormatting() {
            return formatting;
        }
        public ResourceLocation getCapeTexture() {
            return capeTexture;
        }

        public enum ContributionLevel {
            DEVELOPER("developer"),
            GUEST_ARTIST("guest_artist"),
            COMPOSER("composer"),
            CONTRIBUTOR("contributor");

            private final String name;

            ContributionLevel(String name) {
                this.name = name;
            }

            public String getContributionLevelName() {
                return name;
            }
        }
    }

}

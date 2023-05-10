package com.example.imaggenbackend;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class pageExportBuilder {
    // The path to the folder where images must be saved
    static final String IMG_FOLDER_PATH = new File("./images/").getAbsolutePath();

    /**
     * The path where the zip files will be stored
     */
    static final String ZIP_FOLDER_PATH = new File("./images/ZIP Files/").getAbsolutePath();


    public static String[] getImagesInDirectory(String zipID) {

        String directoryPath = IMG_FOLDER_PATH + File.separator + zipID;

        // Create a File object for the directory
        File directory = new File(directoryPath);

        // Return an array of all file names in the directory
        return directory.list();
    }

    public static String getZipPath(String zipID) throws IOException {
        String directoryPath = IMG_FOLDER_PATH + File.separator + zipID;

        String[] imgIDs = getImagesInDirectory(zipID);


        String exportID = Miscellaneous.generateRandomPageExportID();

        String filecontentString = "[ {\n" +
                "  \"operation\" : \"CREATE\",\n" +
                "  \"data\" : {\n" +
                "    \"name\" : \"Attachment\",\n" +
                "    \"id\" :" + exportID + ",\n" +
                "    \"fields\" : {\n" +
                "      \"420003510\" : \"" + imgIDs[2] + "\",\n" +
                "      \"420003512\" : \"customPages\",\n" +
                "      \"420003511\" : \"customPages\",\n" +
                "      \"420003514\" : null,\n" +
                "      \"420003513\" : null,\n" +
                "      \"420003505\" : null,\n" +
                "      \"420003507\" : \"46708\",\n" +
                "      \"420003506\" : \"HR.png\",\n" +
                "      \"420003509\" : \"HR.png\",\n" +
                "      \"420003508\" : \"application/octet-stream\"\n" +
                "    },\n" +
                "    \"localizedFields\" : null,\n" +
                "    \"attachments\" : [ {\n" +
                "      \"fileName\" : \"HR.png\",\n" +
                "      \"fieldId\" : \"420003506\",\n" +
                "      \"contentRef\" : \"" + imgIDs[2] + "\",\n" +
                "      \"content\" : null\n" +
                "    } ]\n" +
                "  },\n" +
                "  \"associations\" : [ ]\n" +
                "}, {\n" +
                "  \"operation\" : \"CREATE\",\n" +
                "  \"data\" : {\n" +
                "    \"name\" : \"Attachment\",\n" +
                "    \"id\" : null,\n" +
                "    \"fields\" : {\n" +
                "      \"420003510\" : \"" + imgIDs[3] + "\",\n" +
                "      \"420003512\" : \"customPages\",\n" +
                "      \"420003511\" : \"customPages\",\n" +
                "      \"420003514\" : null,\n" +
                "      \"420003513\" : null,\n" +
                "      \"420003505\" : null,\n" +
                "      \"420003507\" : \"71769\",\n" +
                "      \"420003506\" : \"Procurement.png\",\n" +
                "      \"420003509\" : \"Procurement.png\",\n" +
                "      \"420003508\" : \"application/octet-stream\"\n" +
                "    },\n" +
                "    \"localizedFields\" : null,\n" +
                "    \"attachments\" : [ {\n" +
                "      \"fileName\" : \"Procurement.png\",\n" +
                "      \"fieldId\" : \"420003506\",\n" +
                "      \"contentRef\" : \"" + imgIDs[3] + "\",\n" +
                "      \"content\" : null\n" +
                "    } ]\n" +
                "  },\n" +
                "  \"associations\" : [ ]\n" +
                "}, {\n" +
                "  \"operation\" : \"CREATE\",\n" +
                "  \"data\" : {\n" +
                "    \"name\" : \"Attachment\",\n" +
                "    \"id\" : null,\n" +
                "    \"fields\" : {\n" +
                "      \"420003510\" : \"" + imgIDs[0] + "\",\n" +
                "      \"420003512\" : \"customPages\",\n" +
                "      \"420003511\" : \"customPages\",\n" +
                "      \"420003514\" : null,\n" +
                "      \"420003513\" : null,\n" +
                "      \"420003505\" : null,\n" +
                "      \"420003507\" : \"22798\",\n" +
                "      \"420003506\" : \"Sales.png\",\n" +
                "      \"420003509\" : \"Sales.png\",\n" +
                "      \"420003508\" : \"application/octet-stream\"\n" +
                "    },\n" +
                "    \"localizedFields\" : null,\n" +
                "    \"attachments\" : [ {\n" +
                "      \"fileName\" : \"Sales.png\",\n" +
                "      \"fieldId\" : \"420003506\",\n" +
                "      \"contentRef\" : \" " + imgIDs[0] + "\",\n" +
                "      \"content\" : null\n" +
                "    } ]\n" +
                "  },\n" +
                "  \"associations\" : [ ]\n" +
                "}, {\n" +
                "  \"operation\" : \"CREATE\",\n" +
                "  \"data\" : {\n" +
                "    \"name\" : \"Attachment\",\n" +
                "    \"id\" : null,\n" +
                "    \"fields\" : {\n" +
                "      \"420003510\" : \"" + imgIDs[1] + "\",\n" +
                "      \"420003512\" : \"customPages\",\n" +
                "      \"420003511\" : \"customPages\",\n" +
                "      \"420003514\" : null,\n" +
                "      \"420003513\" : null,\n" +
                "      \"420003505\" : null,\n" +
                "      \"420003507\" : \"178940\",\n" +
                "      \"420003506\" : \"Marketing.png\",\n" +
                "      \"420003509\" : \"Marketing.png\",\n" +
                "      \"420003508\" : \"application/octet-stream\"\n" +
                "    },\n" +
                "    \"localizedFields\" : null,\n" +
                "    \"attachments\" : [ {\n" +
                "      \"fileName\" : \"Marketing.png\",\n" +
                "      \"fieldId\" : \"420003506\",\n" +
                "      \"contentRef\" : \"" + imgIDs[1] + "\",\n" +
                "      \"content\" : null\n" +
                "    } ]\n" +
                "  },\n" +
                "  \"associations\" : [ ]\n" +
                "}, {\n" +
                "  \"operation\" : \"CREATE\",\n" +
                "  \"data\" : {\n" +
                "    \"name\" : \"Page\",\n" +
                "    \"id\" : null,\n" +
                "    \"fields\" : {\n" +
                "      \"420003505\" : \"studio_page_sample_bimal\",\n" +
                "      \"420003507\" : \"o33npeks\",\n" +
                "      \"420003506\" : \"DRAFT\"\n" +
                "    },\n" +
                "    \"localizedFields\" : {\n" +
                "      \"420003505\" : {\n" +
                "        \"en-US\" : \"studio_page_sample_bimal\",\n" +
                "        \"_\" : \"studio_page_sample_bimal\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"attachments\" : [ ]\n" +
                "  },\n" +
                "  \"associations\" : [ {\n" +
                "    \"name\" : \"PageLayoutCompConfig\",\n" +
                "    \"id\" : null,\n" +
                "    \"fields\" : {\n" +
                "      \"420003510\" : \"{\\\"layout\\\":{\\\"verticalPadding\\\":\\\"normal\\\"},\\\"base\\\":{\\\"background\\\":{\\\"color\\\":null,\\\"image\\\":{\\\"addPlaceholder\\\":false,\\\"allowAction\\\":false}}},\\\"content\\\":{\\\"background\\\":{\\\"color\\\":null,\\\"image\\\":{\\\"addPlaceholder\\\":false,\\\"allowAction\\\":false}}},\\\"palette\\\":{\\\"basePalette\\\":{\\\"id\\\":\\\"default\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[],\\\"content\\\":{\\\"color-primary\\\":\\\"var(--color-primary)\\\",\\\"color-secondary\\\":\\\"var(--color-secondary)\\\",\\\"color-active\\\":\\\"var(--color-active)\\\",\\\"text-primary\\\":\\\"var(--text-default)\\\",\\\"text-default\\\":\\\"var(--text-default)\\\",\\\"text-secondary\\\":\\\"var(--text-secondary)\\\"}}},\\\"customize\\\":false}}\",\n" +
                "      \"420003512\" : null,\n" +
                "      \"420003511\" : \"[]\",\n" +
                "      \"420003514\" : null,\n" +
                "      \"420003513\" : \"4fb726f5-f7c3-a81a-77ab-6dc569310e98\",\n" +
                "      \"420003505\" : \"com.bmc.dwp.dynamic:page-container\",\n" +
                "      \"420003507\" : null,\n" +
                "      \"420003506\" : null,\n" +
                "      \"420003508\" : \"0\"\n" +
                "    },\n" +
                "    \"localizedFields\" : null,\n" +
                "    \"attachments\" : [ ]\n" +
                "  }, {\n" +
                "    \"name\" : \"PageLayoutCompConfig\",\n" +
                "    \"id\" : null,\n" +
                "    \"fields\" : {\n" +
                "      \"420003510\" : \"{\\\"base\\\":{\\\"rowWidth\\\":\\\"content-area\\\",\\\"shadow\\\":{\\\"id\\\":\\\"shadow-0\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"shadow-0\\\"]}},\\\"verticalMargin\\\":\\\"none\\\",\\\"verticalPadding\\\":\\\"normal\\\",\\\"horizontalPadding\\\":\\\"normal\\\"},\\\"background\\\":{\\\"color\\\":null,\\\"image\\\":{\\\"addPlaceholder\\\":false,\\\"allowAction\\\":false}},\\\"border\\\":{\\\"radius\\\":2,\\\"style\\\":\\\"none\\\"},\\\"label\\\":[],\\\"content\\\":{\\\"width\\\":\\\"full\\\",\\\"innerPadding\\\":\\\"normal\\\"}}\",\n" +
                "      \"420003512\" : null,\n" +
                "      \"420003511\" : \"[]\",\n" +
                "      \"420003514\" : \"4fb726f5-f7c3-a81a-77ab-6dc569310e98\",\n" +
                "      \"420003513\" : \"79bdb204-a53b-dd69-fe08-1c16c9ac81c9\",\n" +
                "      \"420003505\" : \"com.bmc.dwp.dynamic:row-block\",\n" +
                "      \"420003507\" : \"0\",\n" +
                "      \"420003506\" : null,\n" +
                "      \"420003508\" : \"0\"\n" +
                "    },\n" +
                "    \"localizedFields\" : null,\n" +
                "    \"attachments\" : [ ]\n" +
                "  }, {\n" +
                "    \"name\" : \"PageLayoutCompConfig\",\n" +
                "    \"id\" : null,\n" +
                "    \"fields\" : {\n" +
                "      \"420003510\" : \"{\\\"base\\\":{\\\"search\\\":{\\\"sources\\\":[\\\"ITEM\\\",\\\"ARTICLE\\\",\\\"ORDER\\\",\\\"APPROVAL\\\",\\\"CALENDAR\\\",\\\"LOCATION\\\",\\\"ASSET\\\",\\\"USER\\\"],\\\"size\\\":\\\"medium\\\",\\\"showSearchIcon\\\":true},\\\"category\\\":{\\\"showCategories\\\":true}},\\\"version\\\":1}\",\n" +
                "      \"420003512\" : \"7\",\n" +
                "      \"420003511\" : \"[]\",\n" +
                "      \"420003514\" : \"79bdb204-a53b-dd69-fe08-1c16c9ac81c9\",\n" +
                "      \"420003513\" : \"8da5f231-ab5c-5fe4-c012-ca2891c112fc\",\n" +
                "      \"420003505\" : \"com.bmc.dwp.dynamic:search-block\",\n" +
                "      \"420003507\" : \"0\",\n" +
                "      \"420003506\" : null,\n" +
                "      \"420003508\" : \"100000\"\n" +
                "    },\n" +
                "    \"localizedFields\" : null,\n" +
                "    \"attachments\" : [ ]\n" +
                "  }, {\n" +
                "    \"name\" : \"PageLayoutCompConfig\",\n" +
                "    \"id\" : null,\n" +
                "    \"fields\" : {\n" +
                "      \"420003510\" : \"{\\\"base\\\":{\\\"rowWidth\\\":\\\"content-area\\\",\\\"shadow\\\":{\\\"id\\\":\\\"shadow-0\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"shadow-0\\\"]}},\\\"verticalMargin\\\":\\\"none\\\",\\\"verticalPadding\\\":\\\"normal\\\",\\\"horizontalPadding\\\":\\\"normal\\\"},\\\"background\\\":{\\\"color\\\":null,\\\"image\\\":{\\\"addPlaceholder\\\":false,\\\"allowAction\\\":false}},\\\"border\\\":{\\\"radius\\\":2,\\\"style\\\":\\\"none\\\"},\\\"label\\\":[],\\\"content\\\":{\\\"width\\\":\\\"full\\\",\\\"innerPadding\\\":\\\"normal\\\"}}\",\n" +
                "      \"420003512\" : null,\n" +
                "      \"420003511\" : \"[]\",\n" +
                "      \"420003514\" : \"4fb726f5-f7c3-a81a-77ab-6dc569310e98\",\n" +
                "      \"420003513\" : \"220851aa-77e5-2452-3446-b6836d33decf\",\n" +
                "      \"420003505\" : \"com.bmc.dwp.dynamic:row-block\",\n" +
                "      \"420003507\" : \"1\",\n" +
                "      \"420003506\" : null,\n" +
                "      \"420003508\" : \"0\"\n" +
                "    },\n" +
                "    \"localizedFields\" : null,\n" +
                "    \"attachments\" : [ ]\n" +
                "  }, {\n" +
                "    \"name\" : \"PageLayoutCompConfig\",\n" +
                "    \"id\" : null,\n" +
                "    \"fields\" : {\n" +
                "      \"420003510\" : \"{\\\"base\\\":{\\\"template\\\":\\\"stacked-callout-block\\\",\\\"height\\\":{\\\"id\\\":\\\"custom\\\",\\\"name\\\":\\\"Custom\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[]}},\\\"customHeight\\\":{\\\"value\\\":\\\"38\\\"},\\\"shadow\\\":{\\\"id\\\":\\\"shadow-2\\\",\\\"name\\\":\\\"2\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"shadow-2\\\"]}},\\\"padding\\\":null},\\\"background\\\":{\\\"color\\\":\\\"var(--color-block-default-background)\\\",\\\"image\\\":{\\\"addPlaceholder\\\":false,\\\"allowAction\\\":false}},\\\"border\\\":{\\\"radius\\\":2,\\\"style\\\":\\\"none\\\"},\\\"blockAction\\\":{\\\"placeholder\\\":null,\\\"fullView\\\":true,\\\"click\\\":\\\"none\\\"},\\\"media\\\":{\\\"base\\\":{\\\"hasContent\\\":true,\\\"position\\\":\\\"top\\\",\\\"size\\\":\\\"media-size-5\\\"},\\\"asset\\\":[{\\\"padding\\\":{\\\"id\\\":\\\"pad-3\\\",\\\"name\\\":\\\"3\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"pad-3\\\"]}},\\\"type\\\":{\\\"id\\\":\\\"image\\\",\\\"name\\\":\\\"\\\"},\\\"image\\\":{\\\"addPlaceholder\\\":true,\\\"allowAction\\\":true,\\\"asset\\\":\\\"52298634-4379-9ca3-c254-f0e966e82af1\\\",\\\"display\\\":{\\\"id\\\":\\\"stretch\\\",\\\"name\\\":\\\"Stretch\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"image-block--background-stretch\\\"]}},\\\"action\\\":{\\\"placeholder\\\":null,\\\"fullView\\\":null,\\\"click\\\":null}}}]},\\\"content\\\":{\\\"verticalAlign\\\":{\\\"id\\\":\\\"middle\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"content-block--y-center\\\"]}},\\\"padding\\\":{\\\"id\\\":\\\"pad-3\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"pad-3\\\"]}},\\\"hasContent\\\":true},\\\"textBlock1\\\":[{\\\"placeholder\\\":\\\"com.bmc.dwp.dynamic.text-block-1\\\",\\\"text\\\":\\\"669be2a5-cc00-4472-c269-02af77bc3ed4\\\",\\\"alignment\\\":\\\"center\\\",\\\"color\\\":\\\"var(--text-default)\\\",\\\"style\\\":{\\\"id\\\":\\\"h1\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"content\\\":\\\"1\\\",\\\"styles\\\":[],\\\"classes\\\":[\\\"text-h1\\\"]}},\\\"weight\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"font-weight-normal\\\"]}},\\\"overflow\\\":{\\\"id\\\":\\\"clipped\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"overflow-hidden\\\"]}},\\\"margin\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[]}}}],\\\"textBlock2\\\":[{\\\"placeholder\\\":\\\"com.bmc.dwp.dynamic.text-block-2\\\",\\\"text\\\":\\\"0915c806-a9a1-0fbd-35fd-0bbace0398ab\\\",\\\"alignment\\\":\\\"center\\\",\\\"color\\\":\\\"var(--text-default)\\\",\\\"style\\\":{\\\"id\\\":\\\"paragraph\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[]}},\\\"weight\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"font-weight-normal\\\"]}},\\\"overflow\\\":{\\\"id\\\":\\\"clipped\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"overflow-hidden\\\"]}},\\\"margin\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[]}}}],\\\"textBlock3\\\":[],\\\"actionsGroup\\\":{\\\"base\\\":{\\\"alignment\\\":\\\"center\\\",\\\"margin\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"pad-top-3\\\"]}},\\\"padding\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"actions-group__wrapper--normal\\\",\\\"actions-group__action--normal\\\"]}}},\\\"action1\\\":[{\\\"placeholder\\\":\\\"Button 1\\\",\\\"fullView\\\":false,\\\"label\\\":\\\"fb9db1be-89d7-418b-8b4f-555fd4fe9842\\\",\\\"click\\\":\\\"none\\\",\\\"style\\\":{\\\"id\\\":\\\"primary\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"btn btn-primary\\\"]}},\\\"size\\\":{\\\"id\\\":\\\"default\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[]}}}],\\\"action2\\\":[],\\\"action3\\\":[],\\\"action4\\\":[]},\\\"version\\\":1}\",\n" +
                "      \"420003512\" : \"7\",\n" +
                "      \"420003511\" : \"[{\\\"locale\\\":\\\"en-US\\\",\\\"values\\\":[{\\\"key\\\":\\\"52298634-4379-9ca3-c254-f0e966e82af1\\\",\\\"value\\\":\\\"{\\\\\\\"name\\\\\\\":\\\\\\\"HR.png\\\\\\\",\\\\\\\"href\\\\\\\":\\\\\\\"/dwp/api/v1.0/pages/media/" + imgIDs[2] + "\\\\\\\"}\\\"},{\\\"key\\\":\\\"669be2a5-cc00-4472-c269-02af77bc3ed4\\\",\\\"value\\\":\\\"\\\\\\\"Human Resources\\\\\\\"\\\"},{\\\"key\\\":\\\"0915c806-a9a1-0fbd-35fd-0bbace0398ab\\\",\\\"value\\\":\\\"\\\\\\\"Timecards, Payroll, Leaves, On-duty absences, etc.\\\\\\\"\\\"},{\\\"key\\\":\\\"fb9db1be-89d7-418b-8b4f-555fd4fe9842\\\",\\\"value\\\":\\\"\\\\\\\"Click here\\\\\\\"\\\"}]}]\",\n" +
                "      \"420003514\" : \"220851aa-77e5-2452-3446-b6836d33decf\",\n" +
                "      \"420003513\" : \"72873502-54fd-4b37-3ef1-21c78298b800\",\n" +
                "      \"420003505\" : \"com.bmc.dwp.dynamic:content-block\",\n" +
                "      \"420003507\" : \"0\",\n" +
                "      \"420003506\" : null,\n" +
                "      \"420003508\" : \"50000\"\n" +
                "    },\n" +
                "    \"localizedFields\" : null,\n" +
                "    \"attachments\" : [ ]\n" +
                "  }, {\n" +
                "    \"name\" : \"PageLayoutCompConfig\",\n" +
                "    \"id\" : null,\n" +
                "    \"fields\" : {\n" +
                "      \"420003510\" : \"{\\\"base\\\":{\\\"template\\\":\\\"stacked-callout-block\\\",\\\"height\\\":{\\\"id\\\":\\\"custom\\\",\\\"name\\\":\\\"Custom\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[]}},\\\"customHeight\\\":{\\\"value\\\":\\\"38\\\"},\\\"shadow\\\":{\\\"id\\\":\\\"shadow-2\\\",\\\"name\\\":\\\"2\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"shadow-2\\\"]}},\\\"padding\\\":null},\\\"background\\\":{\\\"color\\\":\\\"var(--color-block-default-background)\\\",\\\"image\\\":{\\\"addPlaceholder\\\":false,\\\"allowAction\\\":false}},\\\"border\\\":{\\\"radius\\\":2,\\\"style\\\":\\\"none\\\"},\\\"blockAction\\\":{\\\"placeholder\\\":null,\\\"fullView\\\":true,\\\"click\\\":\\\"none\\\"},\\\"media\\\":{\\\"base\\\":{\\\"hasContent\\\":true,\\\"position\\\":\\\"top\\\",\\\"size\\\":\\\"media-size-5\\\"},\\\"asset\\\":[{\\\"padding\\\":{\\\"id\\\":\\\"pad-3\\\",\\\"name\\\":\\\"3\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"pad-3\\\"]}},\\\"type\\\":{\\\"id\\\":\\\"image\\\",\\\"name\\\":\\\"\\\"},\\\"image\\\":{\\\"addPlaceholder\\\":true,\\\"allowAction\\\":true,\\\"asset\\\":\\\"ec9f4200-00c1-107f-9a25-112810a1b2c4\\\",\\\"display\\\":{\\\"id\\\":\\\"stretch\\\",\\\"name\\\":\\\"Stretch\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"image-block--background-stretch\\\"]}},\\\"action\\\":{\\\"placeholder\\\":null,\\\"fullView\\\":null,\\\"click\\\":null}}}]},\\\"content\\\":{\\\"verticalAlign\\\":{\\\"id\\\":\\\"middle\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"content-block--y-center\\\"]}},\\\"padding\\\":{\\\"id\\\":\\\"pad-3\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"pad-3\\\"]}},\\\"hasContent\\\":true},\\\"textBlock1\\\":[{\\\"placeholder\\\":\\\"com.bmc.dwp.dynamic.text-block-1\\\",\\\"text\\\":\\\"1376bfc4-b22d-61f1-b196-bcc4dd4d97ab\\\",\\\"alignment\\\":\\\"center\\\",\\\"color\\\":\\\"var(--text-default)\\\",\\\"style\\\":{\\\"id\\\":\\\"h1\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"content\\\":\\\"1\\\",\\\"styles\\\":[],\\\"classes\\\":[\\\"text-h1\\\"]}},\\\"weight\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"font-weight-normal\\\"]}},\\\"overflow\\\":{\\\"id\\\":\\\"clipped\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"overflow-hidden\\\"]}},\\\"margin\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[]}}}],\\\"textBlock2\\\":[{\\\"placeholder\\\":\\\"com.bmc.dwp.dynamic.text-block-2\\\",\\\"text\\\":\\\"c3d4cffa-0695-65e6-90a8-c1bf5bf00cd4\\\",\\\"alignment\\\":\\\"center\\\",\\\"color\\\":\\\"var(--text-default)\\\",\\\"style\\\":{\\\"id\\\":\\\"paragraph\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[]}},\\\"weight\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"font-weight-normal\\\"]}},\\\"overflow\\\":{\\\"id\\\":\\\"clipped\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"overflow-hidden\\\"]}},\\\"margin\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[]}}}],\\\"textBlock3\\\":[],\\\"actionsGroup\\\":{\\\"base\\\":{\\\"alignment\\\":\\\"center\\\",\\\"margin\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"pad-top-3\\\"]}},\\\"padding\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"actions-group__wrapper--normal\\\",\\\"actions-group__action--normal\\\"]}}},\\\"action1\\\":[{\\\"placeholder\\\":\\\"Button 1\\\",\\\"fullView\\\":false,\\\"label\\\":\\\"a61a5a98-1303-6389-722e-8d1b85384c51\\\",\\\"click\\\":\\\"none\\\",\\\"style\\\":{\\\"id\\\":\\\"primary\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"btn btn-primary\\\"]}},\\\"size\\\":{\\\"id\\\":\\\"default\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[]}}}],\\\"action2\\\":[],\\\"action3\\\":[],\\\"action4\\\":[]},\\\"version\\\":1}\",\n" +
                "      \"420003512\" : \"7\",\n" +
                "      \"420003511\" : \"[{\\\"locale\\\":\\\"en-US\\\",\\\"values\\\":[{\\\"key\\\":\\\"ec9f4200-00c1-107f-9a25-112810a1b2c4\\\",\\\"value\\\":\\\"{\\\\\\\"name\\\\\\\":\\\\\\\"Procurement.png\\\\\\\",\\\\\\\"href\\\\\\\":\\\\\\\"/dwp/api/v1.0/pages/media/" + imgIDs[3] + "\\\\\\\"}\\\"},{\\\"key\\\":\\\"1376bfc4-b22d-61f1-b196-bcc4dd4d97ab\\\",\\\"value\\\":\\\"\\\\\\\"Procurement\\\\\\\"\\\"},{\\\"key\\\":\\\"c3d4cffa-0695-65e6-90a8-c1bf5bf00cd4\\\",\\\"value\\\":\\\"\\\\\\\"Purchasing, Sourcing, Clearance, etc.\\\\\\\"\\\"},{\\\"key\\\":\\\"a61a5a98-1303-6389-722e-8d1b85384c51\\\",\\\"value\\\":\\\"\\\\\\\"Click here\\\\\\\"\\\"}]}]\",\n" +
                "      \"420003514\" : \"220851aa-77e5-2452-3446-b6836d33decf\",\n" +
                "      \"420003513\" : \"3d59aa5b-8ca2-fc61-74eb-c8d3350a73b1\",\n" +
                "      \"420003505\" : \"com.bmc.dwp.dynamic:content-block\",\n" +
                "      \"420003507\" : \"1\",\n" +
                "      \"420003506\" : null,\n" +
                "      \"420003508\" : \"50000\"\n" +
                "    },\n" +
                "    \"localizedFields\" : null,\n" +
                "    \"attachments\" : [ ]\n" +
                "  }, {\n" +
                "    \"name\" : \"PageLayoutCompConfig\",\n" +
                "    \"id\" : null,\n" +
                "    \"fields\" : {\n" +
                "      \"420003510\" : \"{\\\"base\\\":{\\\"rowWidth\\\":\\\"content-area\\\",\\\"shadow\\\":{\\\"id\\\":\\\"shadow-0\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"shadow-0\\\"]}},\\\"verticalMargin\\\":\\\"none\\\",\\\"verticalPadding\\\":\\\"normal\\\",\\\"horizontalPadding\\\":\\\"normal\\\"},\\\"background\\\":{\\\"color\\\":null,\\\"image\\\":{\\\"addPlaceholder\\\":false,\\\"allowAction\\\":false}},\\\"border\\\":{\\\"radius\\\":2,\\\"style\\\":\\\"none\\\"},\\\"label\\\":[],\\\"content\\\":{\\\"width\\\":\\\"full\\\",\\\"innerPadding\\\":\\\"normal\\\"}}\",\n" +
                "      \"420003512\" : null,\n" +
                "      \"420003511\" : \"[]\",\n" +
                "      \"420003514\" : \"4fb726f5-f7c3-a81a-77ab-6dc569310e98\",\n" +
                "      \"420003513\" : \"7f4d9a1e-36d0-d80a-41bf-ddcdc632484e\",\n" +
                "      \"420003505\" : \"com.bmc.dwp.dynamic:row-block\",\n" +
                "      \"420003507\" : \"2\",\n" +
                "      \"420003506\" : null,\n" +
                "      \"420003508\" : \"0\"\n" +
                "    },\n" +
                "    \"localizedFields\" : null,\n" +
                "    \"attachments\" : [ ]\n" +
                "  }, {\n" +
                "    \"name\" : \"PageLayoutCompConfig\",\n" +
                "    \"id\" : null,\n" +
                "    \"fields\" : {\n" +
                "      \"420003510\" : \"{\\\"base\\\":{\\\"template\\\":\\\"stacked-callout-block\\\",\\\"height\\\":{\\\"id\\\":\\\"custom\\\",\\\"name\\\":\\\"Custom\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[]}},\\\"customHeight\\\":{\\\"value\\\":\\\"38\\\"},\\\"shadow\\\":{\\\"id\\\":\\\"shadow-2\\\",\\\"name\\\":\\\"2\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"shadow-2\\\"]}},\\\"padding\\\":null},\\\"background\\\":{\\\"color\\\":\\\"var(--color-block-default-background)\\\",\\\"image\\\":{\\\"addPlaceholder\\\":false,\\\"allowAction\\\":false}},\\\"border\\\":{\\\"radius\\\":2,\\\"style\\\":\\\"none\\\"},\\\"blockAction\\\":{\\\"placeholder\\\":null,\\\"fullView\\\":true,\\\"click\\\":\\\"none\\\"},\\\"media\\\":{\\\"base\\\":{\\\"hasContent\\\":true,\\\"position\\\":\\\"top\\\",\\\"size\\\":\\\"media-size-5\\\"},\\\"asset\\\":[{\\\"padding\\\":{\\\"id\\\":\\\"pad-3\\\",\\\"name\\\":\\\"3\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"pad-3\\\"]}},\\\"type\\\":{\\\"id\\\":\\\"image\\\",\\\"name\\\":\\\"\\\"},\\\"image\\\":{\\\"addPlaceholder\\\":true,\\\"allowAction\\\":true,\\\"asset\\\":\\\"279f40fb-d462-a5ec-2ab3-276936d925db\\\",\\\"display\\\":{\\\"id\\\":\\\"stretch\\\",\\\"name\\\":\\\"Stretch\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"image-block--background-stretch\\\"]}},\\\"action\\\":{\\\"placeholder\\\":null,\\\"fullView\\\":null,\\\"click\\\":null}}}]},\\\"content\\\":{\\\"verticalAlign\\\":{\\\"id\\\":\\\"middle\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"content-block--y-center\\\"]}},\\\"padding\\\":{\\\"id\\\":\\\"pad-3\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"pad-3\\\"]}},\\\"hasContent\\\":true},\\\"textBlock1\\\":[{\\\"placeholder\\\":\\\"com.bmc.dwp.dynamic.text-block-1\\\",\\\"text\\\":\\\"9bbc246f-521d-6e09-c976-fc4cc78a8e11\\\",\\\"alignment\\\":\\\"center\\\",\\\"color\\\":\\\"var(--text-default)\\\",\\\"style\\\":{\\\"id\\\":\\\"h1\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"content\\\":\\\"1\\\",\\\"styles\\\":[],\\\"classes\\\":[\\\"text-h1\\\"]}},\\\"weight\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"font-weight-normal\\\"]}},\\\"overflow\\\":{\\\"id\\\":\\\"clipped\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"overflow-hidden\\\"]}},\\\"margin\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[]}}}],\\\"textBlock2\\\":[{\\\"placeholder\\\":\\\"com.bmc.dwp.dynamic.text-block-2\\\",\\\"text\\\":\\\"e44a5695-6c00-cdc0-f2d5-b0087532de41\\\",\\\"alignment\\\":\\\"center\\\",\\\"color\\\":\\\"var(--text-default)\\\",\\\"style\\\":{\\\"id\\\":\\\"paragraph\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[]}},\\\"weight\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"font-weight-normal\\\"]}},\\\"overflow\\\":{\\\"id\\\":\\\"clipped\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"overflow-hidden\\\"]}},\\\"margin\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[]}}}],\\\"textBlock3\\\":[],\\\"actionsGroup\\\":{\\\"base\\\":{\\\"alignment\\\":\\\"center\\\",\\\"margin\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"pad-top-3\\\"]}},\\\"padding\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"actions-group__wrapper--normal\\\",\\\"actions-group__action--normal\\\"]}}},\\\"action1\\\":[{\\\"placeholder\\\":\\\"Button 1\\\",\\\"fullView\\\":false,\\\"label\\\":\\\"8d2f0526-a811-d97e-2b37-717eb0d9aeec\\\",\\\"click\\\":\\\"none\\\",\\\"style\\\":{\\\"id\\\":\\\"primary\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"btn btn-primary\\\"]}},\\\"size\\\":{\\\"id\\\":\\\"default\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[]}}}],\\\"action2\\\":[],\\\"action3\\\":[],\\\"action4\\\":[]},\\\"version\\\":1}\",\n" +
                "      \"420003512\" : \"7\",\n" +
                "      \"420003511\" : \"[{\\\"locale\\\":\\\"en-US\\\",\\\"values\\\":[{\\\"key\\\":\\\"279f40fb-d462-a5ec-2ab3-276936d925db\\\",\\\"value\\\":\\\"{\\\\\\\"name\\\\\\\":\\\\\\\"Sales.png\\\\\\\",\\\\\\\"href\\\\\\\":\\\\\\\"/dwp/api/v1.0/pages/media/" + imgIDs[0] + "\\\\\\\"}\\\"},{\\\"key\\\":\\\"9bbc246f-521d-6e09-c976-fc4cc78a8e11\\\",\\\"value\\\":\\\"\\\\\\\"Sales\\\\\\\"\\\"},{\\\"key\\\":\\\"e44a5695-6c00-cdc0-f2d5-b0087532de41\\\",\\\"value\\\":\\\"\\\\\\\"Sales reports, Predictions, Upcoming Campaigns, etc.\\\\\\\"\\\"},{\\\"key\\\":\\\"8d2f0526-a811-d97e-2b37-717eb0d9aeec\\\",\\\"value\\\":\\\"\\\\\\\"Click here\\\\\\\"\\\"}]}]\",\n" +
                "      \"420003514\" : \"7f4d9a1e-36d0-d80a-41bf-ddcdc632484e\",\n" +
                "      \"420003513\" : \"47f3d1a3-9b7b-70d2-a92e-de6d17d8fdb6\",\n" +
                "      \"420003505\" : \"com.bmc.dwp.dynamic:content-block\",\n" +
                "      \"420003507\" : \"0\",\n" +
                "      \"420003506\" : null,\n" +
                "      \"420003508\" : \"50000\"\n" +
                "    },\n" +
                "    \"localizedFields\" : null,\n" +
                "    \"attachments\" : [ ]\n" +
                "  }, {\n" +
                "    \"name\" : \"PageLayoutCompConfig\",\n" +
                "    \"id\" : null,\n" +
                "    \"fields\" : {\n" +
                "      \"420003510\" : \"{\\\"base\\\":{\\\"template\\\":\\\"stacked-callout-block\\\",\\\"height\\\":{\\\"id\\\":\\\"custom\\\",\\\"name\\\":\\\"Custom\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[]}},\\\"customHeight\\\":{\\\"value\\\":\\\"38\\\"},\\\"shadow\\\":{\\\"id\\\":\\\"shadow-2\\\",\\\"name\\\":\\\"2\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"shadow-2\\\"]}},\\\"padding\\\":null},\\\"background\\\":{\\\"color\\\":\\\"var(--color-block-default-background)\\\",\\\"image\\\":{\\\"addPlaceholder\\\":false,\\\"allowAction\\\":false}},\\\"border\\\":{\\\"radius\\\":2,\\\"style\\\":\\\"none\\\"},\\\"blockAction\\\":{\\\"placeholder\\\":null,\\\"fullView\\\":true,\\\"click\\\":\\\"none\\\"},\\\"media\\\":{\\\"base\\\":{\\\"hasContent\\\":true,\\\"position\\\":\\\"top\\\",\\\"size\\\":\\\"media-size-5\\\"},\\\"asset\\\":[{\\\"padding\\\":{\\\"id\\\":\\\"pad-3\\\",\\\"name\\\":\\\"3\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"pad-3\\\"]}},\\\"type\\\":{\\\"id\\\":\\\"image\\\",\\\"name\\\":\\\"\\\"},\\\"image\\\":{\\\"addPlaceholder\\\":true,\\\"allowAction\\\":true,\\\"asset\\\":\\\"68fa0976-ddd6-78c9-93da-671aa085504c\\\",\\\"display\\\":{\\\"id\\\":\\\"stretch\\\",\\\"name\\\":\\\"Stretch\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"image-block--background-stretch\\\"]}},\\\"action\\\":{\\\"placeholder\\\":null,\\\"fullView\\\":null,\\\"click\\\":null}}}]},\\\"content\\\":{\\\"verticalAlign\\\":{\\\"id\\\":\\\"middle\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"content-block--y-center\\\"]}},\\\"padding\\\":{\\\"id\\\":\\\"pad-3\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"pad-3\\\"]}},\\\"hasContent\\\":true},\\\"textBlock1\\\":[{\\\"placeholder\\\":\\\"com.bmc.dwp.dynamic.text-block-1\\\",\\\"text\\\":\\\"8debf1e8-e3e3-edec-94d4-4cde9a4b9ee8\\\",\\\"alignment\\\":\\\"center\\\",\\\"color\\\":\\\"var(--text-default)\\\",\\\"style\\\":{\\\"id\\\":\\\"h1\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"content\\\":\\\"1\\\",\\\"styles\\\":[],\\\"classes\\\":[\\\"text-h1\\\"]}},\\\"weight\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"font-weight-normal\\\"]}},\\\"overflow\\\":{\\\"id\\\":\\\"clipped\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"overflow-hidden\\\"]}},\\\"margin\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[]}}}],\\\"textBlock2\\\":[{\\\"placeholder\\\":\\\"com.bmc.dwp.dynamic.text-block-2\\\",\\\"text\\\":\\\"7c3a1ab0-2ebc-dda3-969e-97a1451cbd71\\\",\\\"alignment\\\":\\\"center\\\",\\\"color\\\":\\\"var(--text-default)\\\",\\\"style\\\":{\\\"id\\\":\\\"paragraph\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[]}},\\\"weight\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"font-weight-normal\\\"]}},\\\"overflow\\\":{\\\"id\\\":\\\"clipped\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"overflow-hidden\\\"]}},\\\"margin\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[]}}}],\\\"textBlock3\\\":[],\\\"actionsGroup\\\":{\\\"base\\\":{\\\"alignment\\\":\\\"center\\\",\\\"margin\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"pad-top-3\\\"]}},\\\"padding\\\":{\\\"id\\\":\\\"normal\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"actions-group__wrapper--normal\\\",\\\"actions-group__action--normal\\\"]}}},\\\"action1\\\":[{\\\"placeholder\\\":\\\"Button 1\\\",\\\"fullView\\\":false,\\\"label\\\":\\\"27ff5696-4ece-e004-c124-b0dd7b2603bb\\\",\\\"click\\\":\\\"none\\\",\\\"style\\\":{\\\"id\\\":\\\"primary\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"btn btn-primary\\\"]}},\\\"size\\\":{\\\"id\\\":\\\"default\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[]}}}],\\\"action2\\\":[],\\\"action3\\\":[],\\\"action4\\\":[]},\\\"version\\\":1}\",\n" +
                "      \"420003512\" : \"7\",\n" +
                "      \"420003511\" : \"[{\\\"locale\\\":\\\"en-US\\\",\\\"values\\\":[{\\\"key\\\":\\\"68fa0976-ddd6-78c9-93da-671aa085504c\\\",\\\"value\\\":\\\"{\\\\\\\"name\\\\\\\":\\\\\\\"Marketing.png\\\\\\\",\\\\\\\"href\\\\\\\":\\\\\\\"/dwp/api/v1.0/pages/media/" + imgIDs[1] + "\\\\\\\"}\\\"},{\\\"key\\\":\\\"8debf1e8-e3e3-edec-94d4-4cde9a4b9ee8\\\",\\\"value\\\":\\\"\\\\\\\"Marketing\\\\\\\"\\\"},{\\\"key\\\":\\\"7c3a1ab0-2ebc-dda3-969e-97a1451cbd71\\\",\\\"value\\\":\\\"\\\\\\\"Marketing Campaigns, Platforms, Customer feedbacks, etc.\\\\\\\"\\\"},{\\\"key\\\":\\\"27ff5696-4ece-e004-c124-b0dd7b2603bb\\\",\\\"value\\\":\\\"\\\\\\\"Click here\\\\\\\"\\\"}]}]\",\n" +
                "      \"420003514\" : \"7f4d9a1e-36d0-d80a-41bf-ddcdc632484e\",\n" +
                "      \"420003513\" : \"c2d201ec-9275-d16b-7b87-7707fa51ecdd\",\n" +
                "      \"420003505\" : \"com.bmc.dwp.dynamic:content-block\",\n" +
                "      \"420003507\" : \"1\",\n" +
                "      \"420003506\" : null,\n" +
                "      \"420003508\" : \"50000\"\n" +
                "    },\n" +
                "    \"localizedFields\" : null,\n" +
                "    \"attachments\" : [ ]\n" +
                "  }, {\n" +
                "    \"name\" : \"PageLayoutCompConfig\",\n" +
                "    \"id\" : null,\n" +
                "    \"fields\" : {\n" +
                "      \"420003510\" : \"{\\\"base\\\":{\\\"rowWidth\\\":\\\"content-area\\\",\\\"shadow\\\":{\\\"id\\\":\\\"shadow-0\\\",\\\"name\\\":\\\"\\\",\\\"data\\\":{\\\"styles\\\":[],\\\"classes\\\":[\\\"shadow-0\\\"]}},\\\"verticalMargin\\\":\\\"none\\\",\\\"verticalPadding\\\":\\\"normal\\\",\\\"horizontalPadding\\\":\\\"normal\\\"},\\\"background\\\":{\\\"color\\\":null,\\\"image\\\":{\\\"addPlaceholder\\\":false,\\\"allowAction\\\":false}},\\\"border\\\":{\\\"radius\\\":2,\\\"style\\\":\\\"none\\\"},\\\"label\\\":[],\\\"content\\\":{\\\"width\\\":\\\"full\\\",\\\"innerPadding\\\":\\\"normal\\\"}}\",\n" +
                "      \"420003512\" : null,\n" +
                "      \"420003511\" : \"[]\",\n" +
                "      \"420003514\" : \"4fb726f5-f7c3-a81a-77ab-6dc569310e98\",\n" +
                "      \"420003513\" : \"e0330448-dbf0-1725-8e67-3982333bafcc\",\n" +
                "      \"420003505\" : \"com.bmc.dwp.dynamic:row-block\",\n" +
                "      \"420003507\" : \"3\",\n" +
                "      \"420003506\" : null,\n" +
                "      \"420003508\" : \"0\"\n" +
                "    },\n" +
                "    \"localizedFields\" : null,\n" +
                "    \"attachments\" : [ ]\n" +
                "  }, {\n" +
                "    \"name\" : \"PageLayoutCompConfig\",\n" +
                "    \"id\" : null,\n" +
                "    \"fields\" : {\n" +
                "      \"420003510\" : \"{\\\"options\\\":{\\\"assetGroup\\\":{\\\"id\\\":\\\"4\\\",\\\"data\\\":\\\"SBE\\\"}}}\",\n" +
                "      \"420003512\" : \"7\",\n" +
                "      \"420003511\" : \"[]\",\n" +
                "      \"420003514\" : \"e0330448-dbf0-1725-8e67-3982333bafcc\",\n" +
                "      \"420003513\" : \"ca0aa184-039d-d940-d406-f2bb2fe0de76\",\n" +
                "      \"420003505\" : \"com.bmc.dwp.mystuff:asset-group\",\n" +
                "      \"420003507\" : \"0\",\n" +
                "      \"420003506\" : null,\n" +
                "      \"420003508\" : \"100000\"\n" +
                "    },\n" +
                "    \"localizedFields\" : null,\n" +
                "    \"attachments\" : [ ]\n" +
                "  } ]\n" +
                "} ]";
        String exportManifestString = String.format("""
                {
                  "buildId" : "6",
                  "bundleVersion" : "22.1.06",
                  "completion" : "2023-04-25T12:55:00Z",
                  "content" : {
                    "ids" : [ "%s" ]
                  }
                }""", exportID);

        // Write filecontentString to data.json file
        String dataFilePath = directoryPath + "/data.json";

        FileWriter fileWriter = new FileWriter(dataFilePath);
        fileWriter.write(filecontentString);
        fileWriter.close();

        // Write export_manifest to export.manifest file
        String exportFilePath = directoryPath + "/export.manifest";

        fileWriter = new FileWriter(exportFilePath);
        fileWriter.write(exportManifestString);
        fileWriter.close();

        // Zip the directoryPath and store as a file in IMG_FOLDER_PATH + /images/ZIP Files/
        String zipFilePath = ZIP_FOLDER_PATH + zipID + ".zip";
        FileOutputStream fos = new FileOutputStream(zipFilePath);
        ZipOutputStream zos = new ZipOutputStream(fos);

        // Iterate through files in the directory and add them to the zip file
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);

                    FileInputStream fis = new FileInputStream(file);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, bytesRead);
                    }
                    fis.close();
                    zos.closeEntry();
                }
            }
        }
        zos.close();

        // Return the absolute path of the zip file
        return new File(zipFilePath).getAbsolutePath();

    }

    public static void downloadImage(String imageUrl, int zipID) throws IOException {
        URL url = new URL(imageUrl);
        UUID uuid = UUID.randomUUID();
        File imagesLocation = new File(IMG_FOLDER_PATH + "/" + zipID + "/");
        imagesLocation.mkdirs();
        imagesLocation.createNewFile();
        File fileName = new File(IMG_FOLDER_PATH + "/" + zipID + "/" + uuid);
        InputStream inputStream = url.openStream();
        OutputStream outputStream = new FileOutputStream(fileName);

        byte[] buffer = new byte[2048];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }

        inputStream.close();
        outputStream.close();
        System.out.println("Downloaded " + imageUrl + " " + uuid);

    }

    static class imageRequestResponse {
        int zipID;
        ArrayList<String> URLs;

        public imageRequestResponse(int zipID, ArrayList<String> URLs) {
            this.zipID = zipID;
            this.URLs = URLs;
        }

        public int getZipID() {
            return zipID;
        }

        public void setZipID(int zipID) {
            this.zipID = zipID;
        }

        public ArrayList<String> getURLs() {
            return URLs;
        }

        public void setURLs(ArrayList<String> URLs) {
            this.URLs = URLs;
        }

    }
}

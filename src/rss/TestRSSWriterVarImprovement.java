package rss;

import DB.ErrorMessages;
import DB.PreferenceSettings;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import main.Item;
import main.BottomTextRotationList;
import main.CVSTable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import main.PackageHeader;
import main.PackageHeaderList;

import main.BottomTextRotationANYList;
import main.CategorieSlugList;
import main.ForcastUi;
import main.PostForm;
import main.PostFormList;
import main.RowData;
import main.Tab;
import org.apache.commons.lang3.StringEscapeUtils;

public class TestRSSWriterVarImprovement {

    static PackageHeaderList packageHeaderList;
    static BottomTextRotationList bottomTextRotationList;
    static BottomTextRotationANYList bottomTextRotationANYList;

    static DateFormat dateFormat = null;
    static PreferenceSettings preferenceSetting = null;

    //private static String RSSFEED = "C:\\Users\\Daniel\\JavaApplication2\\feed.xml";
    public static String monthIntToString(int month) {
        month++;
        if (month < 10) {
            return "0" + Integer.toString(month);
        } else {
            return Integer.toString(month);
        }
    }

    public static Date stringToDate(String stringDateFormat) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        //dateFormat
        Date convertedDateFormat = null;
        try {
            convertedDateFormat = dateFormat.parse(stringDateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDateFormat;
    }

    public static String checkIfNull(String input) {
        if (input == null) {
            return "";
        } else {
            return input;
        }
    }

    public static Double checkIfNull(Double input) {
        if (input == null) {
            return 0.0;
        } else {
            return input;
        }
    }

    public static String generateContentEncoded(PostForm form, PackageHeader packageHeader, Item item) {
        //String contentEncoded="";

        //DELETE all the part that doesn't exist
        if (packageHeader.getPackageExplain().isEmpty()) {
            form.deletePackageExplain();
        }
        if (packageHeader.getUlList().isEmpty()) {
            form.deleteUlList();
        }
        if (packageHeader.getPackageNameImage().isEmpty()) {
            form.deletePackageNameImage();
        }
        if (packageHeader.getPackageName2().isEmpty()) {
            form.deletePackageName2();
        }
        if (!packageHeader.isRecommendedPositions()) {
            form.deleteRecommendedPosition();
        }
        if (item.getCvsTable().getiKnowFirstAvg() == null) {
            form.deleteIKnowFirstAverage();
        }
//        if(botText.isEmpty()){
//            form.deleteBottomText();
//        }
        form.removeAllDollarTag();

        //Replace ###VAR###
        //contentEncoded = replaceAllSharpVar3(form.getMainText(),item,packageHeader,botText);
        return (form.getMainText());
    }

    public static String generateCEBotText(PackageHeader packageHeader, Item item) {
        String contentEncoded = "";

        String positionType = "";
        String packageType = "";
        String subpackage = "ANY";;

        BottomTextRotationList choosenBotTextList = null;
        //BottomTextRotation choosenBotText = null;

        positionType = item.getPositionType();
        packageType = item.getPackageContent();
        if (item.getSubpackage() != null) {
            subpackage = item.getSubpackage();
        }

        choosenBotTextList = bottomTextRotationList.getListWhere(positionType, packageType, subpackage);
        String textForANY = bottomTextRotationANYList.getRandomCombination(packageHeader.getPackageName(), packageHeader.getPackageName2(), packageHeader.getSubpackage());
        contentEncoded = choosenBotTextList.pickOne(textForANY).getText();

        return contentEncoded;
    }

    public static String generateExcerptEncoded(PostForm form, PackageHeader packageHeader, Item item) {

        String formModified = form.getExcerptText();

        //DELETE all the part that doesn't exist
        if (packageHeader.getPackageNameImage().isEmpty()) {
            formModified = PostForm.deleteRowWhere(formModified, "packageNameImage");
        }
        if (packageHeader.getPackageName2().isEmpty()) {
            formModified = PostForm.deleteRowWhere(formModified, "packageName2");
        }
        if (!packageHeader.isRecommendedPositions()) {
            formModified = PostForm.deleteRowWhere(formModified, "recommendedPosition");
        }
        if (item.getCvsTable().getiKnowFirstAvg() == null) {
            formModified = PostForm.deleteRowWhere(formModified, "iknowfirstAverage");
        }
        formModified = PostForm.removeAllDollarTag(formModified);

//        formModified = formModified.replaceAll("###POSTLINK###",entryLink);
//        String excerptEncoded = replaceAllSharpVar3(formModified,item,packageHeader,"");
        return formModified;
    }

    public static VarText replaceAllSharpVarMaster(Item item, PackageHeader packageHeader, String titleFormat, String contentFormat, String excerptFormat, String linkFormat, String domain) {
        //title = replaceAll(item)
        VarText varText = null;
        //packageHeader
        packageHeader = replaceAllSharpVarPackageHeader(item, packageHeader);
        //Bottom Text
        String botText = generateCEBotText(packageHeader, item);
        //Title
        String title = replaceAllSharpVarTitle(titleFormat, item, packageHeader);
        //Content
        String content = replaceAllSharpVarContent(contentFormat, item, packageHeader, botText, domain);
        //Link
        String link = replaceAllSharpVarLink(item, linkFormat, title);
        //Excerpt
        String excerpt = replaceAllSharpVarExcerpt(excerptFormat, item, packageHeader, link, domain);

        varText = new VarText(title, content, excerpt, link, packageHeader);
        return varText;
    }

    private static PackageHeader replaceAllSharpVarPackageHeader(Item item, PackageHeader packageHeader) {
        List<String> ulList = packageHeader.getUlList();
        ulList.stream().forEach((li) -> {
            li = replaceAllSharpVarString(li, item);
        });
        packageHeader.setPackageName2(replaceAllSharpVarString(packageHeader.getPackageName2(), item));
        packageHeader.setPackageExplain(replaceAllSharpVarString(packageHeader.getPackageExplain(), item));
        packageHeader.setUlList(ulList);
        
        return packageHeader;
        //replaceAllSharpVarString(packageHeader.getPackageExplain(), item)
    }
    
    private static String replaceAllSharpVarString(String inputText, Item item) {
        String outputText = "";
        CVSTable cvsTable1 = null;
        CVSTable cvsTable2 = null;

        int topN = Integer.parseInt(item.getTopN());

        outputText = inputText.replace("###KEYWORD1###", item.getKeyword1())
                .replace("###KEYWORD2###", item.getKeyword2())
                .replace("###RECOMMENDEDPOSITIONS###", item.getPositionType())
                .replace("###TIMEFRAME###", item.getTimeFrame())
                .replace("###TOPSTOCKDESCRIPTION###", item.getTopStockDescription())
                .replace("###FORECASTDATE###", item.getForecastDay(dateFormat))
                .replace("###FORECASTDATE-DAY###", item.getForecastDateDay())
                .replace("###FORECASTDATE-MONTH###", item.getForecastDateMonth())
                .replace("###FORECASTDATE-YEAR###", item.getForecastDateYear())
                .replace("###TARGETDATE###", item.getTargetDay(dateFormat))
                .replace("###TARGETDATE-DAY###", item.getTargetDateDay())
                .replace("###TARGETDATE-MONTH###", item.getTargetDateMonth())
                .replace("###TARGETDATE-YEAR###", item.getTargetDateYear())
                .replace("###TOPN###", item.getTopN())
                .replace("###TOPN2###", Integer.toString(topN * 2));

        //Long & Short
        if (item.getPositionType().equalsIgnoreCase("Long & Short")) {
            cvsTable1 = item.getCvsTable();
            cvsTable2 = item.getCvsTable2();
            outputText = outputText
                    .replace("###CVSTABLE1-RANK1-SYM###", "<em>" + cvsTable1.getHighest(1).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK2-SYM###", "<em>" + cvsTable1.getHighest(2).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK3-SYM###", "<em>" + cvsTable1.getHighest(3).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK1-RET###", String.format("%.2f", cvsTable1.getHighest(1).getReturnz()))
                    .replace("###CVSTABLE1-RANK2-RET###", String.format("%.2f", cvsTable1.getHighest(2).getReturnz()))
                    .replace("###CVSTABLE1-RANK3-RET###", String.format("%.2f", cvsTable1.getHighest(3).getReturnz()))
                    .replace("###CVSTABLE1-IKFAVG###", String.format("%.2f", cvsTable1.getIKnowFirstAvg().getReturnz()))
                    .replace("###CVSTABLE1-S&P500###", String.format("%.2f", cvsTable1.getSNP500().getReturnz()))
                    .replace("###CVSTABLE1-PREMIUM###", String.format("%.2f", cvsTable1.getIKnowFirstAvg().getReturnz() - cvsTable1.getSNP500().getReturnz()))
                    .replace("###CVSTABLE1-COUNTACCURATE###", Integer.toString(cvsTable1.countAccurate()))
                    .replace("###CVSTABLE1-COUNTTOTALROW###", Integer.toString(cvsTable1.countTotalRow()))
                    .replace("###CVSTABLE2-RANK1-SYM###", "<em>" + cvsTable2.getHighest(1).getSymbol() + "</em>")
                    .replace("###CVSTABLE2-RANK2-SYM###", "<em>" + cvsTable2.getHighest(2).getSymbol() + "</em>")
                    .replace("###CVSTABLE2-RANK3-SYM###", "<em>" + cvsTable2.getHighest(3).getSymbol() + "</em>")
                    .replace("###CVSTABLE2-RANK1-RET###", String.format("%.2f", cvsTable2.getHighest(1).getReturnz()))
                    .replace("###CVSTABLE2-RANK2-RET###", String.format("%.2f", cvsTable2.getHighest(2).getReturnz()))
                    .replace("###CVSTABLE2-RANK3-RET###", String.format("%.2f", cvsTable2.getHighest(3).getReturnz()))
                    .replace("###CVSTABLE2-IKFAVG###", String.format("%.2f", cvsTable2.getIKnowFirstAvg().getReturnz()))
                    .replace("###CVSTABLE2-S&P500###", String.format("%.2f", cvsTable2.getSNP500().getReturnz()))
                    .replace("###CVSTABLE2-PREMIUM###", String.format("%.2f", cvsTable2.getIKnowFirstAvg().getReturnz() - cvsTable2.getSNP500().getReturnz()))
                    .replace("###CVSTABLE2-COUNTACCURATE###", Integer.toString(cvsTable2.countAccurate()))
                    .replace("###CVSTABLE2-COUNTTOTALROW###", Integer.toString(cvsTable2.countTotalRow()));
        } else {
            cvsTable1 = item.getCvsTable();
            outputText = outputText
                    .replace("###CVSTABLE1-RANK1-SYM###", "<em>" + cvsTable1.getHighest(1).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK2-SYM###", "<em>" + cvsTable1.getHighest(2).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK3-SYM###", "<em>" + cvsTable1.getHighest(3).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK1-RET###", String.format("%.2f", cvsTable1.getHighest(1).getReturnz()))
                    .replace("###CVSTABLE1-RANK2-RET###", String.format("%.2f", cvsTable1.getHighest(2).getReturnz()))
                    .replace("###CVSTABLE1-RANK3-RET###", String.format("%.2f", cvsTable1.getHighest(3).getReturnz()))
                    .replace("###CVSTABLE1-IKFAVG###", String.format("%.2f", cvsTable1.getIKnowFirstAvg().getReturnz()))
                    .replace("###CVSTABLE1-S&P500###", String.format("%.2f", cvsTable1.getSNP500().getReturnz()))
                    .replace("###CVSTABLE1-PREMIUM###", String.format("%.2f", cvsTable1.getIKnowFirstAvg().getReturnz() - cvsTable1.getSNP500().getReturnz()))
                    .replace("###CVSTABLE1-COUNTACCURATE###", Integer.toString(cvsTable1.countAccurate()))
                    .replace("###CVSTABLE1-COUNTTOTALROW###", Integer.toString(cvsTable1.countTotalRow()));
        }
        switch (item.getTimeFrame()) {
            case "1 Month":
            case "3 Months":
            case "1 Year":
                outputText = outputText.replace("###TERMLENGHT###", "long-term");
                break;
            default:
                outputText = outputText.replace("###TERMLENGHT###", "short-term");
                break;
        }

        return outputText;
    }

    public static String replaceAllSharpVarTitle(String inputText, Item item, PackageHeader packageHeader) {
        String outputText = "";
        CVSTable cvsTable1 = null;
        CVSTable cvsTable2 = null;

        int topN = Integer.parseInt(item.getTopN());

        outputText = inputText.replace("###KEYWORD1###", item.getKeyword1())
                .replace("###KEYWORD2###", item.getKeyword2())
                .replace("###PACKAGEEXPLAIN###", packageHeader.getPackageExplain())
                .replace("###PACKAGENAME2###", packageHeader.getPackageName2())
                .replace("###RECOMMENDEDPOSITIONS###", item.getPositionType())
                .replace("###TIMEFRAME###", item.getTimeFrame())
                .replace("###TOPSTOCKDESCRIPTION###", item.getTopStockDescription())
                .replace("###FORECASTDATE###", item.getForecastDay(dateFormat))
                .replace("###FORECASTDATE-DAY###", item.getForecastDateDay())
                .replace("###FORECASTDATE-MONTH###", item.getForecastDateMonth())
                .replace("###FORECASTDATE-YEAR###", item.getForecastDateYear())
                .replace("###TARGETDATE###", item.getTargetDay(dateFormat))
                .replace("###TARGETDATE-DAY###", item.getTargetDateDay())
                .replace("###TARGETDATE-MONTH###", item.getTargetDateMonth())
                .replace("###TARGETDATE-YEAR###", item.getTargetDateYear())
                .replace("###TOPN###", item.getTopN())
                .replace("###TOPN2###", Integer.toString(topN * 2));

        //Long & Short
        if (item.getPositionType().equalsIgnoreCase("Long & Short")) {
            cvsTable1 = item.getCvsTable();
            cvsTable2 = item.getCvsTable2();
            List<RowData> csvRowDataList = new ArrayList();
            csvRowDataList.add(cvsTable1.getHighest(1));
            csvRowDataList.add(cvsTable1.getHighest(2));
            csvRowDataList.add(cvsTable1.getHighest(3));
            
            csvRowDataList.add(cvsTable2.getHighest(1));
            csvRowDataList.add(cvsTable2.getHighest(2));
            csvRowDataList.add(cvsTable2.getHighest(3));
            csvRowDataList = csvRowDataList.stream().sorted((i1,i2)->Double.compare(i2.getReturnz(), i1.getReturnz()))
                    .collect(Collectors.toList());
            //csvTableRowDataList.add(cvsTable1.getCvsTable());
            //csvTableRowDataList.add(cvsTable2);
            
            
            outputText = outputText
                    .replace("###CVSTABLE1-RANK1-SYM###", "<em>" + cvsTable1.getHighest(1).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK2-SYM###", "<em>" + cvsTable1.getHighest(2).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK3-SYM###", "<em>" + cvsTable1.getHighest(3).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK1-RET###", String.format("%.2f", cvsTable1.getHighest(1).getReturnz()))
                    .replace("###CVSTABLE1-RANK2-RET###", String.format("%.2f", cvsTable1.getHighest(2).getReturnz()))
                    .replace("###CVSTABLE1-RANK3-RET###", String.format("%.2f", cvsTable1.getHighest(3).getReturnz()))
                    .replace("###CVSTABLE1-IKFAVG###", String.format("%.2f", cvsTable1.getIKnowFirstAvg().getReturnz()))
                    .replace("###CVSTABLE1-S&P500###", String.format("%.2f", cvsTable1.getSNP500().getReturnz()))
                    .replace("###CVSTABLE1-PREMIUM###", String.format("%.2f", cvsTable1.getIKnowFirstAvg().getReturnz() - cvsTable1.getSNP500().getReturnz()))
                    .replace("###CVSTABLE1-COUNTACCURATE###", Integer.toString(cvsTable1.countAccurate()))
                    .replace("###CVSTABLE1-COUNTTOTALROW###", Integer.toString(cvsTable1.countTotalRow()))
                    .replace("###CVSTABLE2-RANK1-SYM###", "<em>" + cvsTable2.getHighest(1).getSymbol() + "</em>")
                    .replace("###CVSTABLE2-RANK2-SYM###", "<em>" + cvsTable2.getHighest(2).getSymbol() + "</em>")
                    .replace("###CVSTABLE2-RANK3-SYM###", "<em>" + cvsTable2.getHighest(3).getSymbol() + "</em>")
                    .replace("###CVSTABLE2-RANK1-RET###", String.format("%.2f", cvsTable2.getHighest(1).getReturnz()))
                    .replace("###CVSTABLE2-RANK2-RET###", String.format("%.2f", cvsTable2.getHighest(2).getReturnz()))
                    .replace("###CVSTABLE2-RANK3-RET###", String.format("%.2f", cvsTable2.getHighest(3).getReturnz()))
                    .replace("###CVSTABLE2-IKFAVG###", String.format("%.2f", cvsTable2.getIKnowFirstAvg().getReturnz()))
                    .replace("###CVSTABLE2-S&P500###", String.format("%.2f", cvsTable2.getSNP500().getReturnz()))
                    .replace("###CVSTABLE2-PREMIUM###", String.format("%.2f", cvsTable2.getIKnowFirstAvg().getReturnz() - cvsTable2.getSNP500().getReturnz()))
                    .replace("###CVSTABLE2-COUNTACCURATE###", Integer.toString(cvsTable2.countAccurate()))
                    .replace("###CVSTABLE2-COUNTTOTALROW###", Integer.toString(cvsTable2.countTotalRow()))
                    
                    .replace("###CVSTABLES-RANK1-SYM###", "<em>" + csvRowDataList.get(0).getSymbol() + "</em>")
                    .replace("###CVSTABLES-RANK2-SYM###", "<em>" + csvRowDataList.get(1).getSymbol() + "</em>")
                    .replace("###CVSTABLES-RANK3-SYM###", "<em>" + csvRowDataList.get(2).getSymbol() + "</em>")
                    .replace("###CVSTABLES-RANK1-RET###", String.format("%.2f", csvRowDataList.get(0).getReturnz()))
                    .replace("###CVSTABLES-RANK2-RET###", String.format("%.2f", csvRowDataList.get(1).getReturnz()))
                    .replace("###CVSTABLES-RANK3-RET###", String.format("%.2f", csvRowDataList.get(2).getReturnz()));
        } else {
            cvsTable1 = item.getCvsTable();
            outputText = outputText
                    .replace("###CVSTABLE1-RANK1-SYM###", "<em>" + cvsTable1.getHighest(1).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK2-SYM###", "<em>" + cvsTable1.getHighest(2).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK3-SYM###", "<em>" + cvsTable1.getHighest(3).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK1-RET###", String.format("%.2f", cvsTable1.getHighest(1).getReturnz()))
                    .replace("###CVSTABLE1-RANK2-RET###", String.format("%.2f", cvsTable1.getHighest(2).getReturnz()))
                    .replace("###CVSTABLE1-RANK3-RET###", String.format("%.2f", cvsTable1.getHighest(3).getReturnz()))
                    .replace("###CVSTABLE1-IKFAVG###", String.format("%.2f", cvsTable1.getIKnowFirstAvg().getReturnz()))
                    .replace("###CVSTABLE1-S&P500###", String.format("%.2f", cvsTable1.getSNP500().getReturnz()))
                    .replace("###CVSTABLE1-PREMIUM###", String.format("%.2f", cvsTable1.getIKnowFirstAvg().getReturnz() - cvsTable1.getSNP500().getReturnz()))
                    .replace("###CVSTABLE1-COUNTACCURATE###", Integer.toString(cvsTable1.countAccurate()))
                    .replace("###CVSTABLE1-COUNTTOTALROW###", Integer.toString(cvsTable1.countTotalRow()));
        }
        switch (item.getTimeFrame()) {
            case "1 Month":
            case "3 Months":
            case "1 Year":
                outputText = outputText.replace("###TERMLENGHT###", "long-term");
                break;
            default:
                outputText = outputText.replace("###TERMLENGHT###", "short-term");
                break;
        }

        return outputText;
    }

    public static String replaceAllSharpVarContent(String inputText, Item item, PackageHeader packageHeader, String bottomText, String domain) {

        String outputText = "";
        CVSTable cvsTable1 = null;
        CVSTable cvsTable2 = null;
        int topN = Integer.parseInt(item.getTopN());
        //UL list
        String ulList = "";
        for (String li : packageHeader.getUlList()) {
            ulList += "<li>" + li + "</li>\n";
        }

        // Big Image
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String month = monthIntToString(Calendar.getInstance().get(Calendar.MONTH));
        String fileName = item.getCvsTable().getFileName();

        String mainImage = "<img class=\"aligncenter wp-image-37141 size-full\" "
                + "src=\"" + domain + "wp-content/uploads/" + year + "/" + month + "/" + fileName.substring(0, fileName.lastIndexOf(".")) + "." + preferenceSetting.getImageExt() + "\" "
                + "alt=\"" + item.getKeyword1() + "\" width=\"" + preferenceSetting.getMainImageWidth() + "\" height=\"" + preferenceSetting.getMainImageHeight() + "\" />";

        outputText = inputText.replace("###BOTTOMTEXT###", bottomText + "\n")
                .replace("###MAINIMAGE###", mainImage);

        outputText = outputText.replace("###KEYWORD1###", item.getKeyword1())
                .replace("###KEYWORD2###", item.getKeyword2())
                .replace("###PACKAGEEXPLAIN###", packageHeader.getPackageExplain())
                .replace("###ULLIST###", ulList)
                .replace("###PACKAGENAMEIMAGE###", packageHeader.getPackageNameImage())
                .replace("###PACKAGENAME2###", packageHeader.getPackageName2())
                .replace("###RECOMMENDEDPOSITIONS###", item.getPositionType())
                .replace("###TIMEFRAME###", item.getTimeFrame())
                .replace("###TOPSTOCKDESCRIPTION###", item.getTopStockDescription())
                .replace("###FORECASTDATE###", item.getForecastDay(dateFormat))
                .replace("###FORECASTDATE-DAY###", item.getForecastDateDay())
                .replace("###FORECASTDATE-MONTH###", item.getForecastDateMonth())
                .replace("###FORECASTDATE-YEAR###", item.getForecastDateYear())
                .replace("###TARGETDATE###", item.getTargetDay(dateFormat))
                .replace("###TARGETDATE-DAY###", item.getTargetDateDay())
                .replace("###TARGETDATE-MONTH###", item.getTargetDateMonth())
                .replace("###TARGETDATE-YEAR###", item.getTargetDateYear())
                .replace("###TOPN###", item.getTopN())
                .replace("###TOPN2###", Integer.toString(topN * 2));

        //Long & Short
        if (item.getPositionType().equalsIgnoreCase("Long & Short")) {
            cvsTable1 = item.getCvsTable();
            cvsTable2 = item.getCvsTable2();
            outputText = outputText
                    .replace("###CVSTABLE1-RANK1-SYM###", "<em>" + cvsTable1.getHighest(1).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK2-SYM###", "<em>" + cvsTable1.getHighest(2).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK3-SYM###", "<em>" + cvsTable1.getHighest(3).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK1-RET###", String.format("%.2f", cvsTable1.getHighest(1).getReturnz()))
                    .replace("###CVSTABLE1-RANK2-RET###", String.format("%.2f", cvsTable1.getHighest(2).getReturnz()))
                    .replace("###CVSTABLE1-RANK3-RET###", String.format("%.2f", cvsTable1.getHighest(3).getReturnz()))
                    .replace("###CVSTABLE1-IKFAVG###", String.format("%.2f", cvsTable1.getIKnowFirstAvg().getReturnz()))
                    .replace("###CVSTABLE1-S&P500###", String.format("%.2f", cvsTable1.getSNP500().getReturnz()))
                    .replace("###CVSTABLE1-PREMIUM###", String.format("%.2f", cvsTable1.getIKnowFirstAvg().getReturnz() - cvsTable1.getSNP500().getReturnz()))
                    .replace("###CVSTABLE1-COUNTACCURATE###", Integer.toString(cvsTable1.countAccurate()))
                    .replace("###CVSTABLE1-COUNTTOTALROW###", Integer.toString(cvsTable1.countTotalRow()))
                    .replace("###CVSTABLE2-RANK1-SYM###", "<em>" + cvsTable2.getHighest(1).getSymbol() + "</em>")
                    .replace("###CVSTABLE2-RANK2-SYM###", "<em>" + cvsTable2.getHighest(2).getSymbol() + "</em>")
                    .replace("###CVSTABLE2-RANK3-SYM###", "<em>" + cvsTable2.getHighest(3).getSymbol() + "</em>")
                    .replace("###CVSTABLE2-RANK1-RET###", String.format("%.2f", cvsTable2.getHighest(1).getReturnz()))
                    .replace("###CVSTABLE2-RANK2-RET###", String.format("%.2f", cvsTable2.getHighest(2).getReturnz()))
                    .replace("###CVSTABLE2-RANK3-RET###", String.format("%.2f", cvsTable2.getHighest(3).getReturnz()))
                    .replace("###CVSTABLE2-IKFAVG###", String.format("%.2f", cvsTable2.getIKnowFirstAvg().getReturnz()))
                    .replace("###CVSTABLE2-S&P500###", String.format("%.2f", cvsTable2.getSNP500().getReturnz()))
                    .replace("###CVSTABLE2-PREMIUM###", String.format("%.2f", cvsTable2.getIKnowFirstAvg().getReturnz() - cvsTable2.getSNP500().getReturnz()))
                    .replace("###CVSTABLE2-COUNTACCURATE###", Integer.toString(cvsTable2.countAccurate()))
                    .replace("###CVSTABLE2-COUNTTOTALROW###", Integer.toString(cvsTable2.countTotalRow()));
        } else {
            cvsTable1 = item.getCvsTable();
            outputText = outputText
                    .replace("###CVSTABLE1-RANK1-SYM###", "<em>" + cvsTable1.getHighest(1).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK2-SYM###", "<em>" + cvsTable1.getHighest(2).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK3-SYM###", "<em>" + cvsTable1.getHighest(3).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK1-RET###", String.format("%.2f", cvsTable1.getHighest(1).getReturnz()))
                    .replace("###CVSTABLE1-RANK2-RET###", String.format("%.2f", cvsTable1.getHighest(2).getReturnz()))
                    .replace("###CVSTABLE1-RANK3-RET###", String.format("%.2f", cvsTable1.getHighest(3).getReturnz()))
                    .replace("###CVSTABLE1-IKFAVG###", String.format("%.2f", cvsTable1.getIKnowFirstAvg().getReturnz()))
                    .replace("###CVSTABLE1-S&P500###", String.format("%.2f", cvsTable1.getSNP500().getReturnz()))
                    .replace("###CVSTABLE1-PREMIUM###", String.format("%.2f", cvsTable1.getIKnowFirstAvg().getReturnz() - cvsTable1.getSNP500().getReturnz()))
                    .replace("###CVSTABLE1-COUNTACCURATE###", Integer.toString(cvsTable1.countAccurate()))
                    .replace("###CVSTABLE1-COUNTTOTALROW###", Integer.toString(cvsTable1.countTotalRow()));
        }
        switch (item.getTimeFrame()) {
            case "1 Month":
            case "3 Months":
            case "1 Year":
                outputText = outputText.replace("###TERMLENGHT###", "long-term");
                break;
            default:
                outputText = outputText.replace("###TERMLENGHT###", "short-term");
                break;
        }

        return outputText;
    }

    public static String replaceAllSharpVarExcerpt(String inputText, Item item, PackageHeader packageHeader, String link, String domain) {
        String outputText = "";
        CVSTable cvsTable1 = null;
        CVSTable cvsTable2 = null;

        int topN = Integer.parseInt(item.getTopN());

        // Big Image
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String month = monthIntToString(Calendar.getInstance().get(Calendar.MONTH));
        String fileName = item.getCvsTable().getFileName();

        String mainImage = "<img class=\"aligncenter wp-image-37141 size-full\" "
                + "src=\"" + domain + "wp-content/uploads/" + year + "/" + month + "/" + fileName.substring(0, fileName.lastIndexOf(".")) + "." + preferenceSetting.getImageExt() + "\" "
                + "alt=\"" + item.getKeyword1() + "\" width=\"" + preferenceSetting.getMainImageWidth() + "\" height=\"" + preferenceSetting.getMainImageHeight() + "\" />";

        
        outputText = inputText.replace("###KEYWORD1###", item.getKeyword1())
                .replace("###KEYWORD2###", item.getKeyword2())
                .replace("###PACKAGEEXPLAIN###", packageHeader.getPackageExplain())
                .replace("###PACKAGENAME2###", packageHeader.getPackageName2())
                .replace("###PACKAGENAMEIMAGE###", packageHeader.getPackageNameImage())
                .replace("###RECOMMENDEDPOSITIONS###", item.getPositionType())
                .replace("###TIMEFRAME###", item.getTimeFrame())
                .replace("###TOPSTOCKDESCRIPTION###", item.getTopStockDescription())
                .replace("###FORECASTDATE###", item.getForecastDay(dateFormat))
                .replace("###FORECASTDATE-DAY###", item.getForecastDateDay())
                .replace("###FORECASTDATE-MONTH###", item.getForecastDateMonth())
                .replace("###FORECASTDATE-YEAR###", item.getForecastDateYear())
                .replace("###TARGETDATE###", item.getTargetDay(dateFormat))
                .replace("###TARGETDATE-DAY###", item.getTargetDateDay())
                .replace("###TARGETDATE-MONTH###", item.getTargetDateMonth())
                .replace("###TARGETDATE-YEAR###", item.getTargetDateYear())
                .replace("###TOPN###", item.getTopN())
                .replace("###TOPN2###", Integer.toString(topN * 2))
                .replace("###MAINIMAGE###", mainImage);

        //Long & Short
        if (item.getPositionType().equalsIgnoreCase("Long & Short")) {
            cvsTable1 = item.getCvsTable();
            cvsTable2 = item.getCvsTable2();
            outputText = outputText
                    .replace("###CVSTABLE1-RANK1-SYM###", "<em>" + cvsTable1.getHighest(1).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK2-SYM###", "<em>" + cvsTable1.getHighest(2).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK3-SYM###", "<em>" + cvsTable1.getHighest(3).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK1-RET###", String.format("%.2f", cvsTable1.getHighest(1).getReturnz()))
                    .replace("###CVSTABLE1-RANK2-RET###", String.format("%.2f", cvsTable1.getHighest(2).getReturnz()))
                    .replace("###CVSTABLE1-RANK3-RET###", String.format("%.2f", cvsTable1.getHighest(3).getReturnz()))
                    .replace("###CVSTABLE1-IKFAVG###", String.format("%.2f", cvsTable1.getIKnowFirstAvg().getReturnz()))
                    .replace("###CVSTABLE1-S&P500###", String.format("%.2f", cvsTable1.getSNP500().getReturnz()))
                    .replace("###CVSTABLE1-PREMIUM###", String.format("%.2f", cvsTable1.getIKnowFirstAvg().getReturnz() - cvsTable1.getSNP500().getReturnz()))
                    .replace("###CVSTABLE1-COUNTACCURATE###", Integer.toString(cvsTable1.countAccurate()))
                    .replace("###CVSTABLE1-COUNTTOTALROW###", Integer.toString(cvsTable1.countTotalRow()))
                    .replace("###CVSTABLE2-RANK1-SYM###", "<em>" + cvsTable2.getHighest(1).getSymbol() + "</em>")
                    .replace("###CVSTABLE2-RANK2-SYM###", "<em>" + cvsTable2.getHighest(2).getSymbol() + "</em>")
                    .replace("###CVSTABLE2-RANK3-SYM###", "<em>" + cvsTable2.getHighest(3).getSymbol() + "</em>")
                    .replace("###CVSTABLE2-RANK1-RET###", String.format("%.2f", cvsTable2.getHighest(1).getReturnz()))
                    .replace("###CVSTABLE2-RANK2-RET###", String.format("%.2f", cvsTable2.getHighest(2).getReturnz()))
                    .replace("###CVSTABLE2-RANK3-RET###", String.format("%.2f", cvsTable2.getHighest(3).getReturnz()))
                    .replace("###CVSTABLE2-IKFAVG###", String.format("%.2f", cvsTable2.getIKnowFirstAvg().getReturnz()))
                    .replace("###CVSTABLE2-S&P500###", String.format("%.2f", cvsTable2.getSNP500().getReturnz()))
                    .replace("###CVSTABLE2-PREMIUM###", String.format("%.2f", cvsTable2.getIKnowFirstAvg().getReturnz() - cvsTable2.getSNP500().getReturnz()))
                    .replace("###CVSTABLE2-COUNTACCURATE###", Integer.toString(cvsTable2.countAccurate()))
                    .replace("###CVSTABLE2-COUNTTOTALROW###", Integer.toString(cvsTable2.countTotalRow()));
        } else {
            cvsTable1 = item.getCvsTable();
            outputText = outputText
                    .replace("###CVSTABLE1-RANK1-SYM###", "<em>" + cvsTable1.getHighest(1).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK2-SYM###", "<em>" + cvsTable1.getHighest(2).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK3-SYM###", "<em>" + cvsTable1.getHighest(3).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK1-RET###", String.format("%.2f", cvsTable1.getHighest(1).getReturnz()))
                    .replace("###CVSTABLE1-RANK2-RET###", String.format("%.2f", cvsTable1.getHighest(2).getReturnz()))
                    .replace("###CVSTABLE1-RANK3-RET###", String.format("%.2f", cvsTable1.getHighest(3).getReturnz()))
                    .replace("###CVSTABLE1-IKFAVG###", String.format("%.2f", cvsTable1.getIKnowFirstAvg().getReturnz()))
                    .replace("###CVSTABLE1-S&P500###", String.format("%.2f", cvsTable1.getSNP500().getReturnz()))
                    .replace("###CVSTABLE1-PREMIUM###", String.format("%.2f", cvsTable1.getIKnowFirstAvg().getReturnz() - cvsTable1.getSNP500().getReturnz()))
                    .replace("###CVSTABLE1-COUNTACCURATE###", Integer.toString(cvsTable1.countAccurate()))
                    .replace("###CVSTABLE1-COUNTTOTALROW###", Integer.toString(cvsTable1.countTotalRow()));
        }
        switch (item.getTimeFrame()) {
            case "1 Month":
            case "3 Months":
            case "1 Year":
                outputText = outputText.replace("###TERMLENGHT###", "long-term");
                break;
            default:
                outputText = outputText.replace("###TERMLENGHT###", "short-term");
                break;
        }

        if (!link.isEmpty()) {
            outputText = outputText.replace("###POSTLINK###", domain + link);
        }

        return outputText;
    }
    private static String returnToLinkFormat(Double input){
        
        String output = String.format("%.2f", input);
        return output.replace(".", " ");
    }
    public static String replaceAllSharpVarLink(Item item, String format, String title) {
        String outputText = "";
        CVSTable cvsTable1 = null;
        CVSTable cvsTable2 = null;
        

        
        int topN = Integer.parseInt(item.getTopN());
        outputText = format.replace("###KEYWORD1###", item.getKeyword1())
                .replace("###KEYWORD2###", item.getKeyword2())
                .replace("###RECOMMENDEDPOSITIONS###", item.getPositionType())
                .replace("###TIMEFRAME###", item.getTimeFrame())
                .replace("###TOPSTOCKDESCRIPTION###", item.getTopStockDescription())
                .replace("###FORECASTDATE###", item.getForecastDay(dateFormat))
                .replace("###FORECASTDATE-DAY###", item.getForecastDateDay())
                .replace("###FORECASTDATE-MONTH###", item.getForecastDateMonth())
                .replace("###FORECASTDATE-YEAR###", item.getForecastDateYear())
                .replace("###TARGETDATE###", item.getTargetDay(dateFormat))
                .replace("###TARGETDATE-DAY###", item.getTargetDateDay())
                .replace("###TARGETDATE-MONTH###", item.getTargetDateMonth())
                .replace("###TARGETDATE-YEAR###", item.getTargetDateYear())
                .replace("###TOPN###", item.getTopN())
                .replace("###TOPN2###", Integer.toString(topN * 2))
                .replace("###PACKAGENAME###", item.getPackageContent())
                .replace("###CVSTABLE1-RANK1-RET###", returnToLinkFormat( item.getCvsTable().getHighest(1).getReturnz()));
        //Long & Short
        if (item.getPositionType().equalsIgnoreCase("Long & Short")) {
            cvsTable1 = item.getCvsTable();
            cvsTable2 = item.getCvsTable2();
            outputText = outputText
                    .replace("###CVSTABLE1-RANK1-SYM###", "<em>" + cvsTable1.getHighest(1).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK2-SYM###", "<em>" + cvsTable1.getHighest(2).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK3-SYM###", "<em>" + cvsTable1.getHighest(3).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK1-RET###", returnToLinkFormat( cvsTable1.getHighest(1).getReturnz()))
                    .replace("###CVSTABLE1-RANK2-RET###", returnToLinkFormat( cvsTable1.getHighest(2).getReturnz()))
                    .replace("###CVSTABLE1-RANK3-RET###", returnToLinkFormat( cvsTable1.getHighest(3).getReturnz()))
                    .replace("###CVSTABLE1-IKFAVG###", returnToLinkFormat( cvsTable1.getIKnowFirstAvg().getReturnz()))
                    .replace("###CVSTABLE1-S&P500###", returnToLinkFormat( cvsTable1.getSNP500().getReturnz()))
                    .replace("###CVSTABLE1-PREMIUM###", returnToLinkFormat( cvsTable1.getIKnowFirstAvg().getReturnz() - cvsTable1.getSNP500().getReturnz()))
                    .replace("###CVSTABLE1-COUNTACCURATE###", Integer.toString(cvsTable1.countAccurate()))
                    .replace("###CVSTABLE1-COUNTTOTALROW###", Integer.toString(cvsTable1.countTotalRow()))
                    .replace("###CVSTABLE2-RANK1-SYM###", "<em>" + cvsTable2.getHighest(1).getSymbol() + "</em>")
                    .replace("###CVSTABLE2-RANK2-SYM###", "<em>" + cvsTable2.getHighest(2).getSymbol() + "</em>")
                    .replace("###CVSTABLE2-RANK3-SYM###", "<em>" + cvsTable2.getHighest(3).getSymbol() + "</em>")
                    .replace("###CVSTABLE2-RANK1-RET###", returnToLinkFormat( cvsTable2.getHighest(1).getReturnz()))
                    .replace("###CVSTABLE2-RANK2-RET###", returnToLinkFormat( cvsTable2.getHighest(2).getReturnz()))
                    .replace("###CVSTABLE2-RANK3-RET###", returnToLinkFormat( cvsTable2.getHighest(3).getReturnz()))
                    .replace("###CVSTABLE2-IKFAVG###", returnToLinkFormat( cvsTable2.getIKnowFirstAvg().getReturnz()))
                    .replace("###CVSTABLE2-S&P500###", returnToLinkFormat( cvsTable2.getSNP500().getReturnz()))
                    .replace("###CVSTABLE2-PREMIUM###", returnToLinkFormat( cvsTable2.getIKnowFirstAvg().getReturnz() - cvsTable2.getSNP500().getReturnz()))
                    .replace("###CVSTABLE2-COUNTACCURATE###", Integer.toString(cvsTable2.countAccurate()))
                    .replace("###CVSTABLE2-COUNTTOTALROW###", Integer.toString(cvsTable2.countTotalRow()));
        } else {
            cvsTable1 = item.getCvsTable();
            outputText = outputText
                    .replace("###CVSTABLE1-RANK1-SYM###", "<em>" + cvsTable1.getHighest(1).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK2-SYM###", "<em>" + cvsTable1.getHighest(2).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK3-SYM###", "<em>" + cvsTable1.getHighest(3).getSymbol() + "</em>")
                    .replace("###CVSTABLE1-RANK1-RET###", returnToLinkFormat( cvsTable1.getHighest(1).getReturnz()))
                    .replace("###CVSTABLE1-RANK2-RET###", returnToLinkFormat( cvsTable1.getHighest(2).getReturnz()))
                    .replace("###CVSTABLE1-RANK3-RET###", returnToLinkFormat( cvsTable1.getHighest(3).getReturnz()))
                    .replace("###CVSTABLE1-IKFAVG###", returnToLinkFormat( cvsTable1.getIKnowFirstAvg().getReturnz()))
                    .replace("###CVSTABLE1-S&P500###", returnToLinkFormat( cvsTable1.getSNP500().getReturnz()))
                    .replace("###CVSTABLE1-PREMIUM###", returnToLinkFormat( cvsTable1.getIKnowFirstAvg().getReturnz() - cvsTable1.getSNP500().getReturnz()))
                    .replace("###CVSTABLE1-COUNTACCURATE###", Integer.toString(cvsTable1.countAccurate()))
                    .replace("###CVSTABLE1-COUNTTOTALROW###", Integer.toString(cvsTable1.countTotalRow()));
        }
        switch (item.getTimeFrame()) {
            case "1 Month":
            case "3 Months":
            case "1 Year":
                outputText = outputText.replace("###TERMLENGHT###", "long-term");
                break;
            default:
                outputText = outputText.replace("###TERMLENGHT###", "short-term");
                break;
        }
        //replace all - to space
        if(outputText.contains("-")){
            outputText = outputText.replace("-", " ");
        }
        if (!title.isEmpty()) {
            title = title.replaceAll("([\\d])[\\.]([\\d])","$1 $2");
            outputText = outputText.replace("###POSTTITLE###", title);
        }
        //replace all Percent stuff
        outputText = outputText.replaceAll("([\\d])[\\.]([\\d])","$1 $2");
        //turn all escape code to special Char
        outputText = StringEscapeUtils.unescapeHtml4(outputText);
        //Get rid of all accents instead of deleting it
        outputText = Normalizer.normalize(outputText, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        //Replace all special Character
        outputText = outputText.replaceAll("[^(\\w|\\s)]","");
        //Replace all space to dash
        outputText = outputText.replaceAll("\\s", "-");
        //More than one dash to one dash
        outputText = outputText.replaceAll("-{2,}","-");
        
        
        outputText = outputText.toLowerCase();
//        outputText = outputText.replace(" ", "-")
//                .replace("\\.", "-")
//                .replace(":", "-")
//                .replace("%", "-")
//                .replace("[+]", "")
//                .replace(".", "-");
//
//        outputText = outputText.replace("--", "-")
//                .replace("---", "-")
//                .replace("----", "-");


//        titleLinkFriendly = title.replace('.','-').replaceAll(" ","-")
//                    .replace(":","-").replace("%","-").replaceAll("--","-").replace("+","").toLowerCase();
//                titleLinkFriendly = titleLinkFriendly.replaceAll(" ", "");
//        outputText = title.replaceAll('.','-')
//                .replaceAll(" ","-")
//                .

        return (outputText);
    }

    public static String generateTitle(Item item, String titleForm) {
        String title = titleForm.replace("###KEYWORD1###", item.getKeyword1())
                .replace("###KEYWORD2###", item.getKeyword2())
                .replace("###CVSTABLE1-RANK1-RET###", String.format("%.2f", item.getCvsTable().getIKnowFirstAvg().getReturnz()))
                .replace("###TIMEFRAME###", item.getTimeFrame())
                .replace("###IKNOWFIRSTAVG###", String.format("%.2f", item.getCvsTable().getIKnowFirstAvg().getReturnz()));

        return title;
    }

    public static void StartWriting(Tab tab, String RSSFEED) {
        //RSSFEED = "C:\\Users\\Daniel\\ForecastAutomation\\feed.xml";// For now! because of access denied
        //Common Var for current language

        packageHeaderList = tab.getPackageHeaderList();
        bottomTextRotationList = tab.bottomTextRotationList;
        bottomTextRotationANYList = tab.bottomTextRotationANYList;
        CategorieSlugList categorieSlugList = tab.categorieSlugList;
        List<Item> itemList = tab.itemList;
        dateFormat = tab.getPreferenceSettings().getDateFormat();
        preferenceSetting = tab.getPreferenceSettings();

        System.out.println("Creation RSS Feed (" + RSSFEED + ")");//파일 주소 이름
        RSSFeed feed = new RSSFeed();
        RSSHeader header = tab.getPreferenceSettings().getRssHeader();

        feed.setHeader(header);

        ArrayList<RSSEntry> entries = new ArrayList<RSSEntry>();
        RSSEntry entry = null;

        int timeOffset = 0;
        itemList = itemList.stream().sorted((s1,s2)->Double.compare(s1.getTop3(tab.getPositionTypeDictionary()).get(0).getReturnz(),
                                                         s2.getTop3(tab.getPositionTypeDictionary()).get(0).getReturnz()))
                        .collect(Collectors.toList());
        tab.itemList = itemList;
        
        List<ForecastData> toForecastDBList = new ArrayList();
        
        for (Item item : itemList) {
            
            //Pre stage - prepare postForm, 
            PostFormList pfList = tab.getPostFormList().getForm(item.getPositionType(), item.getPackageContent(), item.getSubpackage());
            PostForm pf = pfList.pickOne();
            PackageHeader tempPackageHeader = null;
            tempPackageHeader = packageHeaderList.getPackageHeaderWhereNameIs(item.getPackageContent(), item.getSubpackage());

            //Generate all the Text herefirst : title/COntent/ Excerpt /Link
            String contentFormat = generateContentEncoded(pf, tempPackageHeader, item);
            String titleFormat = pf.getTitle();//generateTitle(item,pf.getTitle()); //###POSTTITLELINKFRIENDLY###
            String excerptFormat = generateExcerptEncoded(pf, tempPackageHeader, item);
            String linkFormat = tab.getPreferenceSettings().getLinkFormat();

            //Convert All Var Here !!!
            VarText vartext = replaceAllSharpVarMaster(item, tempPackageHeader, titleFormat, contentFormat, excerptFormat, linkFormat, feed.getHeader().getLink());
            tempPackageHeader = vartext.getPackageHeader();
            item.setModifiedPackageHeader(tempPackageHeader);
            
            String title = vartext.getTitle();
            String stringContentEncoded = vartext.getContent();
            String stringExcerptEncoded = vartext.getExcerpt();
            String link = vartext.getLink();

            // Title ~ Description - START
            entry = new RSSEntry();

            entry.setTitle(title);
            entry.setLink(feed.getHeader().getLink() + link);
            entry.setDcCreator("Dario");
            entry.setGuid("");
            entry.setGuidAttributeIsPermaLink("true");
            entry.setDescription("");

            entry.setContentEncoded(stringContentEncoded);
            entry.setExcerptEncoded(stringExcerptEncoded);

            DateFormat dateFormatPostDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.SECOND, timeOffset * 5);
            Date ModifiedDate = cal.getTime();
            timeOffset++;

            System.out.println(ModifiedDate.toString());
            entry.setWpPostId("");
            entry.setWpPostDate(dateFormatPostDate.format(ModifiedDate));

            dateFormatPostDate.setTimeZone(TimeZone.getTimeZone("GMT"));
            entry.setWpPostDateGmt(dateFormatPostDate.format(ModifiedDate));

            entry.setWpCommentStatus("closed");
            entry.setWpPingStatus("closed");
            entry.setWpPostName(link);
            entry.setWpStatus("private");
            entry.setWpPostParent("0");
            entry.setWpMenuOrder("0");
            entry.setWpPostType("post");
            entry.setWpPostPassword("");
            entry.setWpIsSticky("0");

            List<String> categoryList = item.getCvsTable().getSymboleWhereAccuracyOne();
            List<String> niceNameList = item.getCvsTable().getSymboleWhereAccuracyOneLowerCase();
            List<String> domainList = new ArrayList();

            if (item.getPositionType().equalsIgnoreCase("Long & Short")) {
                categoryList.addAll(item.getCvsTable2().getSymboleWhereAccuracyOne());
                niceNameList.addAll(item.getCvsTable2().getSymboleWhereAccuracyOneLowerCase());
            }

            for (int i = 0; i < categoryList.size(); i++) {
                domainList.add("post_tag");
            }

            String slugName = categorieSlugList.getMatchSlug(item.getPackageContent());

            categoryList.add(item.getPackageContent());
            niceNameList.add(slugName);
            domainList.add("category");

            entry.setCategory(categoryList);
            entry.setCategoryAttributeNiceName(niceNameList);
            entry.setCategoryAttributeDomain(domainList);

            //WP:POSTMETA
            List<String> metaKeyList = new ArrayList();
            List<String> metaValueList = new ArrayList();

//            metaKeyList.add("_edit_last");
//            metaValueList.add("1");
            metaKeyList.add("_yoast_wpseo_focuskw_text_input");
            metaValueList.add(item.getKeyword1());
            metaKeyList.add("_yoast_wpseo_focuskw");
            metaValueList.add(item.getKeyword1());
//            metaKeyList.add("_yoast_wpseo_linkdex");
//            metaValueList.add("73");
//            metaKeyList.add("_yoast_wpseo_primary_category");
//            metaValueList.add("2014");
//            metaKeyList.add("_jetpack_related_posts_cache");
//            metaValueList.add("a:1:{s:32:\"8f6677c9d6b0f903e98ad32ec61f8deb\";a:2:{s:7:\"expires\";i:1465693912;s:7:\"payload\";a:3:{i:0;a:1:{s:2:\"id\";i:36998;}i:1;a:1:{s:2:\"id\";i:37249;}i:2;a:1:{s:2:\"id\";i:37474;}}}}");
//            metaKeyList.add("_wpas_done_all");
//            metaValueList.add("1");
//            metaKeyList.add("videolink");
//            metaValueList.add("");

            entry.setMetaKey(metaKeyList);
            entry.setMetaValue(metaValueList);

            entries.add(entry);// ++
            toForecastDBList.add(new ForecastData(item, entry));
        }
        /*
        //Item 2
        entry = new RSSEntry();
        entry.setTitle("Java : Display a TIF");
        entry.setDescription("Using JAI, how to display a TIF file");
        entry.setGuid("http://www.rgagnon.com/javadetails/java-0605.html");
        entry.setLink("http://www.rgagnon.com/javadetails/java-0605.html");
        //entry.setPubDate(RSSFeed.formatDate(Calendar.getInstance()));
        entries.add(entry);// ++
         */
        feed.setEntries(entries);

        try {
            ForcastUi.consoleLog("Creating XML: " + RSSFEED);
            RSSWriter.write(feed, RSSFEED);
            ForcastUi.consoleLog("Done Creating XML: " + RSSFEED);
            System.out.println("Done");

            //ForcastUi.consoleLog(": "+RSSFEED);
            //Save to DB
//            tab.updateForecastDataManager1();
//            tab.updateForecastDataManager2(entries);
            tab.updateForecastDBList(toForecastDBList);
            tab.storeForecastDataList();
            tab.emptyForecastDataList();

        } catch (FileNotFoundException e) {
            ErrorMessages.printErrorMsg(ErrorMessages.FILENOTFOUND, RSSFEED);
            ForcastUi.consoleLog(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            ErrorMessages.printErrorMsg(ErrorMessages.FILECOR, RSSFEED);
            ForcastUi.consoleLog(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            ErrorMessages.printErrorMsg(ErrorMessages.FILECOR, RSSFEED);
            ForcastUi.consoleLog(e.getMessage());
            e.printStackTrace();
        }

    }

}

class VarText {

    /**
     * @return the packageHeader
     */
    public PackageHeader getPackageHeader() {
        return packageHeader;
    }

    /**
     * @param packageHeader the packageHeader to set
     */
    public void setPackageHeader(PackageHeader packageHeader) {
        this.packageHeader = packageHeader;
    }

    private String title;
    private String content;
    private String excerpt;
    private String link;
    private PackageHeader packageHeader;
    VarText(String title, String content, String excerpt, String link,PackageHeader packageHeader) {
        this.title = title;
        this.content = content;
        this.excerpt = excerpt;
        this.link = link;
        this.packageHeader = packageHeader;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @return the excerpt
     */
    public String getExcerpt() {
        return excerpt;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

}

package rss;

import DB.ErrorMessages;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
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
import main.PackageHeader;
import main.PackageHeaderList;

import main.BottomTextRotationANYList;
import main.CategorieSlugList;
import main.PostForm;
import main.PostFormList;
import main.Tab;

public class TestRSSWriter {
    static PackageHeaderList packageHeaderList;
    static BottomTextRotationList bottomTextRotationList;
    static BottomTextRotationANYList bottomTextRotationANYList;
    
    //private static String RSSFEED = "C:\\Users\\Daniel\\JavaApplication2\\feed.xml";
    public static String monthIntToString(int month){
        month++;
        if(month<10)
            return "0"+Integer.toString(month);
        else
            return Integer.toString(month);
    }
    public static Date stringToDate(String stringDateFormat){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        //dateFormat
        Date convertedDateFormat = null;
        try{
            convertedDateFormat = dateFormat.parse(stringDateFormat);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDateFormat;
    }
    public static String checkIfNull(String input){
        if(input == null)
            return "";
        else
            return input;
    }
    public static Double checkIfNull(Double input){
        if(input == null)
            return 0.0;
        else
            return input;
    }
    
    
    public static String generateContentEncoded2(PostForm form, PackageHeader packageHeader, Item item){
        String contentEncoded="";
        String botText = generateCEBotText(packageHeader,item);
        
        //DELETE all the part that doesn't exist
        if(packageHeader.getPackageExplain().isEmpty()){
            form.deletePackageExplain();
        }
        if(packageHeader.getUlList().isEmpty()){
            form.deleteUlList();
        }
        if(packageHeader.getPackageNameImage().isEmpty()){
            form.deletePackageNameImage();
        }
        if(packageHeader.getPackageName2().isEmpty()){
            form.deletePackageName2();
        }
        if(!packageHeader.isRecommendedPositions()){
            form.deleteRecommendedPosition();
        }
        if(item.getCvsTable().getiKnowFirstAvg()==null){
            form.deleteIKnowFirstAverage();
        }
        if(botText.isEmpty()){
            form.deleteBottomText();
        }
        form.removeAllDollarTag();
        
        
        //Replace ###VAR###
        contentEncoded = replaceAllSharpVar3(form.getMainText(),item,packageHeader,botText);
        
        return contentEncoded; 
    }
    
    public static String generateCEBotText(PackageHeader packageHeader, Item item){
        String contentEncoded = "";
        
        String positionType = "";
        String packageType = "";
        String subpackage = "ANY";;
        
        BottomTextRotationList choosenBotTextList = null;
        //BottomTextRotation choosenBotText = null;
        
        positionType = item.getPositionType();
        packageType = item.getPackageContent();
        if(item.getSubpackage() != null){
            subpackage = item.getSubpackage();
        }
        
        choosenBotTextList = bottomTextRotationList.getListWhere(positionType,packageType,subpackage);
        String textForANY = bottomTextRotationANYList.getRandomCombination(packageHeader.getPackageName(), packageHeader.getPackageName2(), packageHeader.getSubpackage());
        contentEncoded = choosenBotTextList.pickOne(textForANY).getText();

        //누굴 아래에 둘까 생각하지 마라! 누굴 위에 둘지 생각해라!(멘토)
        //contentEncoded = replaceAllSharpVar(choosenBotText.getText(), item, packageHeader.getPackageName2());
        
//<editor-fold defaultstate="collapsed" desc="comment">
/*
boolean gold =false;
if(item.getSubpackage() != null){
if(item.getSubpackage().equalsIgnoreCase("Gold"))
gold = true;
}



if(gold){

contentEncoded += "<h2 style=\"text-align: center;\"><a href=\"http://lpage.gold-prediction.com/\">"
+ "View more gold forecasts</a></h2>\n<h2><strong>How to interpret this diagram:</strong></h2>\n"
+ "<strong>Algorithmic Stock Forecast:</strong> The table on the left is the stock forecast produced by "
+ "I Know First's algorithm. Each day, subscribers receive forecasts for six different time horizons. "
+ "The top ten stocks in the 1-month forecast may be different than those in the 1-year forecast. "
+ "In the included table, only the relevant tickers have been included. "
+ "A green box represents a positive forecast while a red represents a negative forecast. "
+ "The boxes are then arranged according to their respective signal and predictability values "
+ "(see below for detailed definitions).\n\n"
+ "<strong>Forecast Performance:</strong> The table on the right compares the actual stock performance "
+ "with I Know First's prediction. The column titled \"Forecast\" shows which direction the algorithm "
+ "predicted, and the column \"% Change\" shows the actual stock performance over the indicated time period. "
+ "The \"Accuracy\" column shows a \"v\" if the algorithm correctly predicted the direction of "
+ "the stock or an \"x\" if the forecast was incorrect. The I Know First Average is the equal-weights"
+ " average percent change of the stocks listed below, and the S&amp;P 500 may be included for reference if "
+ "relevant.\n<strong>Signal: </strong>This indicator represents the predicted movement direction/trend; "
+ "not a percentage or specific target price. The signal strength indicates how much the current price "
+ "deviates from what the system considers an equilibrium or “fair” price.\n"
+ "<strong>Predictability:</strong> This value is obtained by calculating the correlation between "
+ "the current prediction and the actual asset movement for each discrete time period. The algorithm "
+ "then averages the results of all the prediction points, while giving more weight to recent performance. "
+ "As the machine keeps learning, the predictability values generally increase.\n\n";
}else if(item.getPackageContent().equalsIgnoreCase("Currency Forecast")){ // Ordinary Path
contentEncoded +="<a href=\"http://currency-prediction.com/\"><strong>View More Currency Forecasts</strong></a>\n" +
"<h2><strong>How to interpret this diagram:</strong></h2>\n" +
"<strong>Algorithmic Currency Forecast:</strong> The table on the left is the forex forecast for the forex outlook, produced by I Know First's algorithm. Each day, subscribers receive forecasts for six different time horizons. The currencies in the 1-month forecast may be different than those in the 1-year forecast. In the included table, only the relevant currencies have been included. A green box represents a positive forecast while a red represents a negative forecast. The boxes are then arranged according to their respective signal and predictability values (see below for detailed definitions).\n" +
"<strong>Forecast Performance: </strong>The table on the right compares the actual currency performance with I Know First's prediction. The column titled \"Forecast\" shows which direction the algorithm predicted, and the column \"% Change\" shows the actual currency's performance over the indicated time period. The \"Accuracy\" column shows a \"v\" if the algorithm correctly predicted the direction of the stock or an \"x\" if the forecast was incorrect. The \"I Know First Hit Ratio\" represents the algorithm's accuracy when predicting the trend of the currency.\n" +
"<strong>Signal: </strong>This indicator represents the predicted movement direction/trend; not a percentage or specific target price. The signal strength indicates how much the current price deviates from what the system considers an equilibrium or “fair” price.\n" +
"<strong>Predictability:</strong> This value is obtained by calculating the correlation between the current prediction and the actual asset movement for each discrete time period. The algorithm then averages the results of all the prediction points, while giving more weight to recent performance. As the machine keeps learning, the values of P generally increase.\n" +
"<strong>Please note-for trading decisions use the most recent forecast. </strong><a href=\"http://lpage.iknowfirst.com/\"><strong>Get today’s forecast and Top stock picks.</strong></a>\n\n";

}else if(item.getPositionType().equalsIgnoreCase("Long & Short")){
contentEncoded += "During the "+item.getTimeFrame()+" forecast, the algorithm had predicted high returns for those "
+ "seeking stock advice. The best performance in the short position came from "
+ "<em>"+item.getCvsTable2().getHighest(1).getSymbol()+"</em> which registered a return of "
+ item.getCvsTable2().getHighest(1).getReturnz()+"%. For the long positions the largest growth was registered by "
+ "<em>"+item.getCvsTable().getHighest(1).getSymbol()+"</em> with a return of "+item.getCvsTable().getHighest(1).getReturnz()
+ "%, during the same period. The package itself, had an overall average return of "+item.getCvsTable().getIKnowFirstAvg().getReturnz()+"%, "
+ "in the long position, providing a premium of "+ (item.getCvsTable().getIKnowFirstAvg().getReturnz()+item.getCvsTable().getSNP500().getReturnz())
+ "% over the SP500's return of "+item.getCvsTable().getSNP500().getReturnz()+"%. With regards to the short position, "
+ "the package had an overall average return of "+item.getCvsTable2().getIKnowFirstAvg().getReturnz()+"%, "
+ "providing investors with a premium of "+ (item.getCvsTable2().getIKnowFirstAvg().getReturnz()+item.getCvsTable2().getSNP500().getReturnz())
+ "% over SP500's return of "+item.getCvsTable2().getSNP500().getReturnz()+"%. The I Know First's Stock Market Algorithm accurately "
+ "forecasted "+item.getCvsTable().countAccurate()+" out of "+item.getCvsTable().countTotalRow()+" stocks, for the long position, "
+ "and "+item.getCvsTable2().countAccurate()+" out of "+item.getCvsTable2().countTotalRow()+" stocks, for the short position for this "+item.getTimeFrame()+" forecasted period.\n\n";


contentEncoded += "<p>" +item.getTopStockDescription()+"</p>"+"\n\n";

contentEncoded += "<em>Algorithmic traders utilize these daily forecasts by the I Know First market prediction system as a tool "
+ "to enhance portfolio performance, verify their own analysis and act on market opportunities faster. This forecast was sent "
+ "to current I Know First algorithmic traders.</em> <h2 class=\"p1\" style=\"text-align: center;\">"
+ "<a href=\"http://www.google.com/url?q=http%3A%2F%2Fiknowfirst.com%2Fstock-market-predictions-how-to-interpret-the-i-know-first-diagram&amp;sa=D&amp;sntz=1&amp;usg=AFQjCNF_g-b97PierJdmMImH2PsgQwJxUw\">"
+ "How to interpret this diagram</a></h2><strong>Please note-for trading decisions use the most recent forecast. </strong>"
+ "<a href=\"http://lpage.iknowfirst.com\"><strong><span style=\"color: #0000ff;\">Get today’s forecast and Top stock picks.</span></strong></a>]]>\n\n";
}else{
contentEncoded +=  item.getCvsTable().countAccurate()+" out of "+item.getCvsTable().countTotalRow()
+" top stock picks from the algorithm increased as predicted for this short-term forecasting period. "
+ item.getCvsTable().getHighest(1).getSymbol()+" saw monumental growth of "+item.getCvsTable().getHighest(1).getReturnz()
+ "% in just "+ item.getTimeFrame() +". "+item.getCvsTable().getHighest(2).getSymbol()+", "
+ "and "+item.getCvsTable().getHighest(3).getSymbol()+" also had excellent performances with returns of "
+  item.getCvsTable().getHighest(2).getReturnz()+"% and "+item.getCvsTable().getHighest(3).getReturnz()+"%"
+ " respectively. This "+item.getTimeFrame()+" forecast had overall growth of "+item.getCvsTable().getIKnowFirstAvg().getReturnz()
+ "%";

if(item.getCvsTable().getSNP500()!=null)
contentEncoded+= " offering investors an advantage over the S&P 500's "+ item.getCvsTable().getSNP500State()
+ " return of "+item.getCvsTable().getSNP500().getReturnz()+"%.\n\n";
else
contentEncoded+=".";

contentEncoded += "<p>" +item.getTopStockDescription()+"</p>"+"\n\n";

contentEncoded += "<em>Algorithmic traders utilize these daily forecasts by the I Know First market prediction system as a tool "
+ "to enhance portfolio performance, verify their own analysis and act on market opportunities faster. This forecast was sent "
+ "to current I Know First algorithmic traders.</em> <h2 class=\"p1\" style=\"text-align: center;\">"
+ "<a href=\"http://www.google.com/url?q=http%3A%2F%2Fiknowfirst.com%2Fstock-market-predictions-how-to-interpret-the-i-know-first-diagram&amp;sa=D&amp;sntz=1&amp;usg=AFQjCNF_g-b97PierJdmMImH2PsgQwJxUw\">"
+ "How to interpret this diagram</a></h2><strong>Please note-for trading decisions use the most recent forecast. </strong>"
+ "<a href=\"http://lpage.iknowfirst.com\"><strong><span style=\"color: #0000ff;\">Get today’s forecast and Top stock picks.</span></strong></a>\n\n";
}

if(item.getPositionType().equalsIgnoreCase("Short"))
contentEncoded = contentEncoded.replace("increased", "decreased");

switch(item.getTimeFrame()){
ase"1 Year":
contentEncoded = contentEncoded.replace("short-term", "long-term");
break;
default:
break; case"3 Months":
case"1 Year":
contentEncoded = contentEncoded.replace("short-term", "long-term");
break;
default:
break;

}*/
//</editor-fold>
        return contentEncoded;        
    }
    
    public static String generateExcerptEncoded2(PostForm form, PackageHeader packageHeader, Item item, String entryLink){
        
        String formModified = form.getExcerptText();
        
        //DELETE all the part that doesn't exist
        if(packageHeader.getPackageNameImage().isEmpty()){
            formModified = PostForm.deleteRowWhere(formModified, "packageNameImage");
        }
        if(packageHeader.getPackageName2().isEmpty()){
            formModified = PostForm.deleteRowWhere(formModified, "packageName2");
        }
        if(!packageHeader.isRecommendedPositions()){
            formModified = PostForm.deleteRowWhere(formModified, "recommendedPosition");
        }
        if(item.getCvsTable().getiKnowFirstAvg()==null){
            formModified = PostForm.deleteRowWhere(formModified, "iknowfirstAverage");
        }
        formModified = PostForm.removeAllDollarTag(formModified);
        
        
        formModified = formModified.replaceAll("###POSTLINK###",entryLink);
        String excerptEncoded = replaceAllSharpVar3(formModified,item,packageHeader,"");
        
        return excerptEncoded;
    }
    public static String replaceAllShparVarLink(Item item, String format, String title){
        String outputText= "";
        int topN = Integer.parseInt(item.getTopN());
        outputText = format.replaceAll("###KEYWORD1###", item.getKeyword1())
                .replaceAll("###RECOMMENDEDPOSITIONS###", item.getPositionType())
                .replaceAll("###TIMEFRAME###", item.getTimeFrame())
                .replaceAll("###TOPSTOCKDESCRIPTION###", item.getTopStockDescription())
                .replaceAll("###FORECASTDATE###", item.getForecastDay())
                .replaceAll("###FORECASTDATE-DAY###", item.getForecastDateDay())
                .replaceAll("###FORECASTDATE-MONTH###", item.getForecastDateMonth())
                .replaceAll("###FORECASTDATE-YEAR###", item.getForecastDateYear())
                .replaceAll("###TARGETDATE###", item.getTargetDay())
                .replaceAll("###TARGETDATE-DAY###", item.getTargetDateDay())
                .replaceAll("###TARGETDATE-MONTH###", item.getTargetDateMonth())
                .replaceAll("###TARGETDATE-YEAR###", item.getTargetDateYear())
                .replaceAll("###TOPN###",item.getTopN())
                .replace("###TOPN2###",Integer.toString(topN*2))
                .replace("###PACKAGENAME###", item.getPackageContent())
                .replace("###CVSTABLE1-RANK1-RET###", String.format("%.2f",item.getCvsTable().getHighest(1).getReturnz()));
        
        if(!title.isEmpty())
            outputText = outputText.replace("###POSTTITLE###", title);
        
        outputText = outputText.toLowerCase();
        outputText = outputText.replaceAll(" ", "-")
                .replaceAll("\\.","-")
                .replaceAll(":","-")
                .replaceAll("%","-")
                .replace("+","");
        
        outputText = outputText.replaceAll("--", "-")
                .replaceAll("---", "-")
                .replaceAll("----", "-");
//        titleLinkFriendly = title.replace('.','-').replaceAll(" ","-")
//                    .replace(":","-").replace("%","-").replaceAll("--","-").replace("+","").toLowerCase();
//                titleLinkFriendly = titleLinkFriendly.replaceAll(" ", "");
//        outputText = title.replaceAll('.','-')
//                .replaceAll(" ","-")
//                .
        
        return outputText;
    }
    public static String replaceAllSharpVar3(String inputText, Item item, PackageHeader packageHeader, String bottomText){
        String outputText= "";
        CVSTable cvsTable1 = null;
        CVSTable cvsTable2 = null;
        int topN = Integer.parseInt(item.getTopN());
        //UL list
        String ulList = "";
        for(String li: packageHeader.getUlList()){
            ulList += "<li>"+li+"</li>\n";
        }
        // Big Image
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String month = monthIntToString(Calendar.getInstance().get(Calendar.MONTH));
        String fileName = item.getCvsTable().getFileName();
        
        String mainImage = "<img class=\"aligncenter wp-image-37141 size-full\" "
                    + "src=\"http://iknowfirst.com/wp-content/uploads/"+year+"/"+month+"/"+fileName.substring(0, fileName.lastIndexOf("."))+".jpg\" "
                    + "alt=\""+ item.getKeyword1() +"\" width=\"960\" height=\"820\" />";
        String test = packageHeader.getPackageExplain();
        outputText = inputText.replace("###BOTTOMTEXT###", bottomText+"\n")
                        .replace("###MAINIMAGE###", mainImage);
        outputText = outputText.replace("###KEYWORD1###", item.getKeyword1())
                .replace("###PACKAGEEXPLAIN###", packageHeader.getPackageExplain())
                .replace("###ULLIST###", ulList)
                .replace("###PACKAGENAMEIMAGE###", packageHeader.getPackageNameImage())
                .replace("###PACKAGENAME2###", packageHeader.getPackageName2())
                .replace("###RECOMMENDEDPOSITIONS###", item.getPositionType())
                .replace("###TIMEFRAME###", item.getTimeFrame())
                .replace("###TOPSTOCKDESCRIPTION###", item.getTopStockDescription())
                .replace("###FORECASTDATE###", item.getForecastDay())
                .replace("###FORECASTDATE-DAY###", item.getForecastDateDay())
                .replace("###FORECASTDATE-MONTH###", item.getForecastDateMonth())
                .replace("###FORECASTDATE-YEAR###", item.getForecastDateYear())
                .replace("###TARGETDATE###", item.getTargetDay())
                .replace("###TARGETDATE-DAY###", item.getTargetDateDay())
                .replace("###TARGETDATE-MONTH###", item.getTargetDateMonth())
                .replace("###TARGETDATE-YEAR###", item.getTargetDateYear())
                .replace("###TOPN###",item.getTopN())
                .replace("###TOPN2###",Integer.toString(topN*2));
        
        //Long & Short
        if(item.getPositionType().equalsIgnoreCase("Long & Short")){
            cvsTable1 = item.getCvsTable();
            cvsTable2 = item.getCvsTable2();
            outputText = outputText                
                .replace("###CVSTABLE1-RANK1-SYM###", "<em>" + cvsTable1.getHighest(1).getSymbol() + "</em>")
                .replace("###CVSTABLE1-RANK2-SYM###", "<em>" + cvsTable1.getHighest(2).getSymbol() + "</em>")
                .replace("###CVSTABLE1-RANK3-SYM###", "<em>" + cvsTable1.getHighest(3).getSymbol() + "</em>")
                .replace("###CVSTABLE1-RANK1-RET###", String.format("%.2f",cvsTable1.getHighest(1).getReturnz()))
                .replace("###CVSTABLE1-RANK2-RET###", String.format("%.2f",cvsTable1.getHighest(2).getReturnz()))
                .replace("###CVSTABLE1-RANK3-RET###", String.format("%.2f",cvsTable1.getHighest(3).getReturnz()))
                .replace("###CVSTABLE1-IKFAVG###", String.format("%.2f",cvsTable1.getIKnowFirstAvg().getReturnz()))
                .replace("###CVSTABLE1-S&P500###", String.format("%.2f",cvsTable1.getSNP500().getReturnz()))
                .replace("###CVSTABLE1-PREMIUM###", String.format("%.2f",cvsTable1.getIKnowFirstAvg().getReturnz()-cvsTable1.getSNP500().getReturnz()))
                .replace("###CVSTABLE1-COUNTACCURATE###", Integer.toString(cvsTable1.countAccurate()))
                .replace("###CVSTABLE1-COUNTTOTALROW###", Integer.toString(cvsTable1.countTotalRow()))
                        
                .replace("###CVSTABLE2-RANK1-SYM###", "<em>" + cvsTable2.getHighest(1).getSymbol() + "</em>")
                .replace("###CVSTABLE2-RANK2-SYM###", "<em>" + cvsTable2.getHighest(2).getSymbol() + "</em>")
                .replace("###CVSTABLE2-RANK3-SYM###", "<em>" + cvsTable2.getHighest(3).getSymbol() + "</em>")
                .replace("###CVSTABLE2-RANK1-RET###", String.format("%.2f",cvsTable2.getHighest(1).getReturnz()))
                .replace("###CVSTABLE2-RANK2-RET###", String.format("%.2f",cvsTable2.getHighest(2).getReturnz()))
                .replace("###CVSTABLE2-RANK3-RET###", String.format("%.2f",cvsTable2.getHighest(3).getReturnz()))
                .replace("###CVSTABLE2-IKFAVG###", String.format("%.2f",cvsTable2.getIKnowFirstAvg().getReturnz()))
                .replace("###CVSTABLE2-S&P500###", String.format("%.2f",cvsTable2.getSNP500().getReturnz()))
                .replace("###CVSTABLE2-PREMIUM###", String.format("%.2f",cvsTable2.getIKnowFirstAvg().getReturnz()-cvsTable2.getSNP500().getReturnz()))
                .replace("###CVSTABLE2-COUNTACCURATE###", Integer.toString(cvsTable2.countAccurate()))
                .replace("###CVSTABLE2-COUNTTOTALROW###", Integer.toString(cvsTable2.countTotalRow()));
        }else{
            cvsTable1 = item.getCvsTable();
            outputText = outputText                
                .replace("###CVSTABLE1-RANK1-SYM###", "<em>" + cvsTable1.getHighest(1).getSymbol() + "</em>")
                .replace("###CVSTABLE1-RANK2-SYM###", "<em>" + cvsTable1.getHighest(2).getSymbol() + "</em>")
                .replace("###CVSTABLE1-RANK3-SYM###", "<em>" + cvsTable1.getHighest(3).getSymbol() + "</em>")
                .replace("###CVSTABLE1-RANK1-RET###", String.format("%.2f",cvsTable1.getHighest(1).getReturnz()))
                .replace("###CVSTABLE1-RANK2-RET###", String.format("%.2f",cvsTable1.getHighest(2).getReturnz()))
                .replace("###CVSTABLE1-RANK3-RET###", String.format("%.2f",cvsTable1.getHighest(3).getReturnz()))
                .replace("###CVSTABLE1-IKFAVG###", String.format("%.2f",cvsTable1.getIKnowFirstAvg().getReturnz()))
                .replace("###CVSTABLE1-S&P500###", String.format("%.2f",cvsTable1.getSNP500().getReturnz()))
                .replace("###CVSTABLE1-PREMIUM###", String.format("%.2f",cvsTable1.getIKnowFirstAvg().getReturnz()-cvsTable1.getSNP500().getReturnz()))
                .replace("###CVSTABLE1-COUNTACCURATE###", Integer.toString(cvsTable1.countAccurate()))
                .replace("###CVSTABLE1-COUNTTOTALROW###", Integer.toString(cvsTable1.countTotalRow()));
        }
        switch(item.getTimeFrame()){
            case"1 Month":
            case"3 Months":
            case"1 Year":
                outputText = outputText.replace("###TERMLENGHT###", "long-term");
                break;
            default:
                outputText = outputText.replace("###TERMLENGHT###", "short-term");
                break; 
        }
        
        return outputText;
    }
    public static String replaceAllSharpVar2(String inputText, Item item, String packageName2){
        String outputText= "";
        CVSTable cvsTable1 = null;
        CVSTable cvsTable2 = null;
        int topN = Integer.parseInt(item.getTopN());
            
                                
        System.out.println(packageName2);
        //Common Replaces
        outputText = inputText.replaceAll("###FORECASTDATE###", item.getForecastDay())
                        .replaceAll("###FORECASTDATE-DAY###", item.getForecastDateDay())
                        .replaceAll("###FORECASTDATE-MONTH###", item.getForecastDateMonth())
                        .replaceAll("###FORECASTDATE-YEAR###", item.getForecastDateYear())
                        .replaceAll("###TARGETDATE###", item.getTargetDay())
                        .replaceAll("###TARGETDATE-DAY###", item.getTargetDateDay())
                        .replaceAll("###TARGETDATE-MONTH###", item.getTargetDateMonth())
                        .replaceAll("###TARGETDATE-YEAR###", item.getTargetDateYear())
                        .replaceAll("###TOPN###",item.getTopN())
                        .replace("###TOPN2###",Integer.toString(topN*2));
        
        //Long & Short
        if(item.getPositionType().equalsIgnoreCase("Long & Short")){
            cvsTable1 = item.getCvsTable();
            cvsTable2 = item.getCvsTable2();
            outputText = outputText.replaceAll("###TIMEFRAME###", item.getTimeFrame())
                .replaceAll("###PACKAGENAME2###", packageName2)
                .replaceAll("###TOPSTOCKDESCRIPTION###", item.getTopStockDescription())
                
                .replaceAll("###CVSTABLE1-RANK1-SYM###", "<em>" + cvsTable1.getHighest(1).getSymbol() + "</em>")
                .replaceAll("###CVSTABLE1-RANK2-SYM###", "<em>" + cvsTable1.getHighest(2).getSymbol() + "</em>")
                .replaceAll("###CVSTABLE1-RANK3-SYM###", "<em>" + cvsTable1.getHighest(3).getSymbol() + "</em>")
                .replaceAll("###CVSTABLE1-RANK1-RET###", String.format("%.2f",cvsTable1.getHighest(1).getReturnz()))
                .replaceAll("###CVSTABLE1-RANK2-RET###", String.format("%.2f",cvsTable1.getHighest(2).getReturnz()))
                .replaceAll("###CVSTABLE1-RANK3-RET###", String.format("%.2f",cvsTable1.getHighest(3).getReturnz()))
                .replaceAll("###CVSTABLE1-IKFAVG###", String.format("%.2f",cvsTable1.getIKnowFirstAvg().getReturnz()))
                .replaceAll("###CVSTABLE1-S&P500###", String.format("%.2f",cvsTable1.getSNP500().getReturnz()))
                .replaceAll("###CVSTABLE1-PREMIUM###", String.format("%.2f",cvsTable1.getIKnowFirstAvg().getReturnz()-cvsTable1.getSNP500().getReturnz()))
                .replaceAll("###CVSTABLE1-COUNTACCURATE###", Integer.toString(cvsTable1.countAccurate()))
                .replaceAll("###CVSTABLE1-COUNTTOTALROW###", Integer.toString(cvsTable1.countTotalRow()))
                        
                .replaceAll("###CVSTABLE2-RANK1-SYM###", "<em>" + cvsTable2.getHighest(1).getSymbol() + "</em>")
                .replaceAll("###CVSTABLE2-RANK2-SYM###", "<em>" + cvsTable2.getHighest(2).getSymbol() + "</em>")
                .replaceAll("###CVSTABLE2-RANK3-SYM###", "<em>" + cvsTable2.getHighest(3).getSymbol() + "</em>")
                .replaceAll("###CVSTABLE2-RANK1-RET###", String.format("%.2f",cvsTable2.getHighest(1).getReturnz()))
                .replaceAll("###CVSTABLE2-RANK2-RET###", String.format("%.2f",cvsTable2.getHighest(2).getReturnz()))
                .replaceAll("###CVSTABLE2-RANK3-RET###", String.format("%.2f",cvsTable2.getHighest(3).getReturnz()))
                .replaceAll("###CVSTABLE2-IKFAVG###", String.format("%.2f",cvsTable2.getIKnowFirstAvg().getReturnz()))
                .replaceAll("###CVSTABLE2-S&P500###", String.format("%.2f",cvsTable2.getSNP500().getReturnz()))
                .replaceAll("###CVSTABLE2-PREMIUM###", String.format("%.2f",cvsTable2.getIKnowFirstAvg().getReturnz()-cvsTable2.getSNP500().getReturnz()))
                .replaceAll("###CVSTABLE2-COUNTACCURATE###", Integer.toString(cvsTable2.countAccurate()))
                .replaceAll("###CVSTABLE2-COUNTTOTALROW###", Integer.toString(cvsTable2.countTotalRow()));
        }else{
            cvsTable1 = item.getCvsTable();
            outputText = outputText.replaceAll("###TIMEFRAME###", item.getTimeFrame())
                .replaceAll("###PACKAGENAME2###", packageName2)
                .replaceAll("###TOPSTOCKDESCRIPTION###", item.getTopStockDescription())
                
                .replaceAll("###CVSTABLE1-RANK1-SYM###", "<em>" + cvsTable1.getHighest(1).getSymbol() + "</em>")
                .replaceAll("###CVSTABLE1-RANK2-SYM###", "<em>" + cvsTable1.getHighest(2).getSymbol() + "</em>")
                .replaceAll("###CVSTABLE1-RANK3-SYM###", "<em>" + cvsTable1.getHighest(3).getSymbol() + "</em>")
                .replaceAll("###CVSTABLE1-RANK1-RET###", String.format("%.2f",cvsTable1.getHighest(1).getReturnz()))
                .replaceAll("###CVSTABLE1-RANK2-RET###", String.format("%.2f",cvsTable1.getHighest(2).getReturnz()))
                .replaceAll("###CVSTABLE1-RANK3-RET###", String.format("%.2f",cvsTable1.getHighest(3).getReturnz()))
                .replaceAll("###CVSTABLE1-IKFAVG###", String.format("%.2f",cvsTable1.getIKnowFirstAvg().getReturnz()))
                .replaceAll("###CVSTABLE1-S&P500###", String.format("%.2f",cvsTable1.getSNP500().getReturnz()))
                .replaceAll("###CVSTABLE1-PREMIUM###", String.format("%.2f",cvsTable1.getIKnowFirstAvg().getReturnz()-cvsTable1.getSNP500().getReturnz()))
                .replaceAll("###CVSTABLE1-COUNTACCURATE###", Integer.toString(cvsTable1.countAccurate()))
                .replaceAll("###CVSTABLE1-COUNTTOTALROW###", Integer.toString(cvsTable1.countTotalRow()));
        }
        switch(item.getTimeFrame()){
            case"1 Month":
            case"3 Months":
            case"1 Year":
                outputText = outputText.replace("###TERMLENGHT###", "long-term");
                break;
            default:
                outputText = outputText.replace("###TERMLENGHT###", "short-term");
                break; 
        }
        
        return outputText;
    }
    public static String replaceAllSharpVar(String inputText, Item item, String packageName2){
        String outputText= "";
        CVSTable cvsTable1 = null;
        CVSTable cvsTable2 = null;
        
        System.out.println(packageName2);
        //Common Replaces
        outputText = inputText.replaceAll("###FORECASTDATE###", item.getForecastDay())
                        .replaceAll("###FORECASTDATE-DAY###", item.getForecastDay())
                        .replaceAll("###FORECASTDATE-MONTH###", item.getForecastDay())
                        .replaceAll("###FORECASTDATE-YEAR###", item.getForecastDay())
                        .replaceAll("###TARGETDATE###", item.getForecastDay())
                        .replaceAll("###TARGETDATE-DAY###", item.getForecastDay())
                        .replaceAll("###TARGETDATE-MONTH###", item.getForecastDay())
                        .replaceAll("###TARGETDATE-YEAR###", item.getForecastDay());
        
        //Long & Short
        if(item.getPositionType().equalsIgnoreCase("Long & Short")){
            cvsTable1 = item.getCvsTable();
            cvsTable2 = item.getCvsTable2();
            outputText = outputText.replaceAll("###TIMEFRAME###", item.getTimeFrame())
                .replaceAll("###PACKAGENAME2###", packageName2)
                .replaceAll("###TOPSTOCKDESCRIPTION###", item.getTopStockDescription())
                
                .replaceAll("###CVSTABLE1-RANK1-SYM###", "<em>" + cvsTable1.getHighest(1).getSymbol() + "</em>")
                .replaceAll("###CVSTABLE1-RANK2-SYM###", "<em>" + cvsTable1.getHighest(2).getSymbol() + "</em>")
                .replaceAll("###CVSTABLE1-RANK3-SYM###", "<em>" + cvsTable1.getHighest(3).getSymbol() + "</em>")
                .replaceAll("###CVSTABLE1-RANK1-RET###", String.format("%.2f",cvsTable1.getHighest(1).getReturnz()))
                .replaceAll("###CVSTABLE1-RANK2-RET###", String.format("%.2f",cvsTable1.getHighest(2).getReturnz()))
                .replaceAll("###CVSTABLE1-RANK3-RET###", String.format("%.2f",cvsTable1.getHighest(3).getReturnz()))
                .replaceAll("###CVSTABLE1-IKFAVG###", String.format("%.2f",cvsTable1.getIKnowFirstAvg().getReturnz()))
                .replaceAll("###CVSTABLE1-S&P500###", String.format("%.2f",cvsTable1.getSNP500().getReturnz()))
                .replaceAll("###CVSTABLE1-PREMIUM###", String.format("%.2f",cvsTable1.getIKnowFirstAvg().getReturnz()-cvsTable1.getSNP500().getReturnz()))
                .replaceAll("###CVSTABLE1-COUNTACCURATE###", Integer.toString(cvsTable1.countAccurate()))
                .replaceAll("###CVSTABLE1-COUNTTOTALROW###", Integer.toString(cvsTable1.countTotalRow()))
                        
                .replaceAll("###CVSTABLE2-RANK1-SYM###", "<em>" + cvsTable2.getHighest(1).getSymbol() + "</em>")
                .replaceAll("###CVSTABLE2-RANK2-SYM###", "<em>" + cvsTable2.getHighest(2).getSymbol() + "</em>")
                .replaceAll("###CVSTABLE2-RANK3-SYM###", "<em>" + cvsTable2.getHighest(3).getSymbol() + "</em>")
                .replaceAll("###CVSTABLE2-RANK1-RET###", String.format("%.2f",cvsTable2.getHighest(1).getReturnz()))
                .replaceAll("###CVSTABLE2-RANK2-RET###", String.format("%.2f",cvsTable2.getHighest(2).getReturnz()))
                .replaceAll("###CVSTABLE2-RANK3-RET###", String.format("%.2f",cvsTable2.getHighest(3).getReturnz()))
                .replaceAll("###CVSTABLE2-IKFAVG###", String.format("%.2f",cvsTable2.getIKnowFirstAvg().getReturnz()))
                .replaceAll("###CVSTABLE2-S&P500###", String.format("%.2f",cvsTable2.getSNP500().getReturnz()))
                .replaceAll("###CVSTABLE2-PREMIUM###", String.format("%.2f",cvsTable2.getIKnowFirstAvg().getReturnz()-cvsTable2.getSNP500().getReturnz()))
                .replaceAll("###CVSTABLE2-COUNTACCURATE###", Integer.toString(cvsTable2.countAccurate()))
                .replaceAll("###CVSTABLE2-COUNTTOTALROW###", Integer.toString(cvsTable2.countTotalRow()));
        }else{
            cvsTable1 = item.getCvsTable();
            outputText = outputText.replaceAll("###TIMEFRAME###", item.getTimeFrame())
                .replaceAll("###PACKAGENAME2###", packageName2)
                .replaceAll("###TOPSTOCKDESCRIPTION###", item.getTopStockDescription())
                
                .replaceAll("###CVSTABLE1-RANK1-SYM###", "<em>" + cvsTable1.getHighest(1).getSymbol() + "</em>")
                .replaceAll("###CVSTABLE1-RANK2-SYM###", "<em>" + cvsTable1.getHighest(2).getSymbol() + "</em>")
                .replaceAll("###CVSTABLE1-RANK3-SYM###", "<em>" + cvsTable1.getHighest(3).getSymbol() + "</em>")
                .replaceAll("###CVSTABLE1-RANK1-RET###", String.format("%.2f",cvsTable1.getHighest(1).getReturnz()))
                .replaceAll("###CVSTABLE1-RANK2-RET###", String.format("%.2f",cvsTable1.getHighest(2).getReturnz()))
                .replaceAll("###CVSTABLE1-RANK3-RET###", String.format("%.2f",cvsTable1.getHighest(3).getReturnz()))
                .replaceAll("###CVSTABLE1-IKFAVG###", String.format("%.2f",cvsTable1.getIKnowFirstAvg().getReturnz()))
                .replaceAll("###CVSTABLE1-S&P500###", String.format("%.2f",cvsTable1.getSNP500().getReturnz()))
                .replaceAll("###CVSTABLE1-PREMIUM###", String.format("%.2f",cvsTable1.getIKnowFirstAvg().getReturnz()-cvsTable1.getSNP500().getReturnz()))
                .replaceAll("###CVSTABLE1-COUNTACCURATE###", Integer.toString(cvsTable1.countAccurate()))
                .replaceAll("###CVSTABLE1-COUNTTOTALROW###", Integer.toString(cvsTable1.countTotalRow()));
        }
        switch(item.getTimeFrame()){
            case"1 Month":
            case"3 Months":
            case"1 Year":
                outputText = outputText.replace("###TERMLENGHT###", "long-term");
                break;
            default:
                outputText = outputText.replace("###TERMLENGHT###", "short-term");
                break; 
        }
        
        return outputText;
    }
    public static String generateTitle(Item item){
        String title = "";
        
        switch(item.getPackageContent()){
            case "Currency Forecast":
                title = item.getKeyword1()+" "+ item.getKeyword2() + ": Almost "
                    + String.format("%.2f",item.getCvsTable().getIKnowFirstAvg().getReturnz())  +"% Hit Ratio in "+item.getTimeFrame();
                break;
            default:
                title = item.getKeyword1()+" "+ item.getKeyword2() + ": Returns up to "
                    + String.format("%.2f",item.getTopStock()) +"% in "+item.getTimeFrame();
        }
        return title;
    }
    public static String generateTitle2(Item item, String titleForm){
        String title = titleForm.replace("###KEYWORD1###", item.getKeyword1())
                .replace("###KEYWORD2###", item.getKeyword2())
                .replace("###CVSTABLE1-RANK1-RET###", String.format("%.2f",item.getCvsTable().getIKnowFirstAvg().getReturnz()))
                .replace("###TIMEFRAME###", item.getTimeFrame())
                .replace("###IKNOWFIRSTAVG###", String.format("%.2f",item.getCvsTable().getIKnowFirstAvg().getReturnz()));
        
        
        return title;
    }
    public static void StartWriting(Tab tab,String RSSFEED) {
        //RSSFEED = "C:\\Users\\Daniel\\ForecastAutomation\\feed.xml";// For now! because of access denied
        //Common Var for current language
        
        packageHeaderList = tab.getPackageHeaderList();
        bottomTextRotationList = tab.bottomTextRotationList;
        bottomTextRotationANYList = tab.bottomTextRotationANYList;
        CategorieSlugList categorieSlugList = tab.categorieSlugList;
        List<Item> itemList = tab.itemList;
        
                
        System.out.println("Creation RSS Feed (" + RSSFEED + ")");//파일 주소 이름
        RSSFeed feed  = new RSSFeed();
        RSSHeader header = tab.getPreferenceSettings().getRssHeader();
//        
//        //
//        // HEADER PART
//        //
//        RSSHeader header = new RSSHeader();
//        header.setTitle("Stock Forecast Based On a Predictive Algorithm | I Know First  |");
//        header.setLink("http://iknowfirst.com");
//        header.setDescription("Stock Forecast Based On a Predictive Algorithm. Contact Us: contact@iknowfirst.com");
//        header.setLanguage("en-US");
//        header.setWpWxrVersion("1.2");
//        header.setWpBaseSiteUrl("http://iknowfirst.com");
//        header.setWpBaseBlogUrl("http://iknowfirst.com");
//        //<wp:author>
//        header.setWpAuthorId("51");
//        header.setWpAuthorLogin("Dario");
//        header.setWpAuthorEmail("dariobiasini@gmail.com");
//        header.setWpAuthorDisplayName("Dario Biasini");
//        header.setWpAuthorFirstName("Dario");
//        header.setWpAuthorLastName("Biasini");
//        //</wp:author>

        feed.setHeader(header);
        
        ArrayList<RSSEntry> entries = new ArrayList<RSSEntry>();
        RSSEntry entry = null;
        
        for(Item item : itemList){
            
            // Title ~ Description - START
            entry = new RSSEntry();
            PostFormList pfList = tab.getPostFormList().getForm(item.getPositionType(), item.getPackageContent(), item.getSubpackage());
            PostForm pf = pfList.pickOne();
            String title = generateTitle2(item,pf.getTitle()); //###POSTTITLELINKFRIENDLY###
            //String title = generateTitle(item);
            entry.setTitle(title);
            
            String linkFormat = tab.getPreferenceSettings().getLinkFormat();
            String link= "";
//            String titleLinkFriendly = "";
//            if(linkFormat.contains("###POSTTITLELINKFRIENDLY###")){
//                titleLinkFriendly = title.replace('.','-').replaceAll(" ","-")
//                    .replace(":","-").replace("%","-").replaceAll("--","-").replace("+","").toLowerCase();
//                titleLinkFriendly = titleLinkFriendly.replaceAll(" ", "");
//            }
            
            link = replaceAllShparVarLink(item,linkFormat,title);
            
            //String link = 

            //titleLinkForm = "fr-"+titleLinkForm;
            
            
            entry.setLink(feed.getHeader().getLink()+link);
            entry.setDcCreator("Dario");
            entry.setGuid("");
            entry.setGuidAttributeIsPermaLink("true");
            entry.setDescription("");
            // Title ~ Description - END
            
            
            //
            // Content Encoded - Start
            //
            
            PackageHeader tempPackageHeader= null;
            tempPackageHeader = packageHeaderList.getPackageHeaderWhereNameIs(item.getPackageContent(),item.getSubpackage());

//            if(item.getPackageContent().equalsIgnoreCase("Gold & Commodities")){
//                tempPackageHeader = null;
//            }else{
//                tempPackageHeader = packageHeaderList.getPackageHeaderWhereNameIs(item.getPackageContent());
//                if(tempPackageHeader == null){
//                    System.out.println("The Package You are looking for doesn't exist");
//                    return;
//                }
//            }
            //Content Encoded
            String stringContentEncoded = generateContentEncoded2(pf, tempPackageHeader, item);
            //String stringContentEncoded = generateContentEncoded(tempPackageHeader, item);
            
            entry.setContentEncoded(stringContentEncoded);
            
            //Excerpt Encoded
            //String stringExcerptEncoded = generateExcerptEncoded(tempPackageHeader, item,entry.getLink());
            String stringExcerptEncoded = generateExcerptEncoded2(pfList.pickOne(), tempPackageHeader, item,entry.getLink());
            entry.setExcerptEncoded(stringExcerptEncoded);
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            
            entry.setWpPostId("");
            entry.setWpPostDate(dateFormat.format(date));
            
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            entry.setWpPostDateGmt(dateFormat.format(date));
            
            entry.setWpCommentStatus("closed");
            entry.setWpPingStatus("closed");
            entry.setWpPostName(link);
            entry.setWpStatus("private");
            entry.setWpPostParent("0");
            entry.setWpMenuOrder("0");
            entry.setWpPostType("post");
            entry.setWpPostPassword("");
            entry.setWpIsSticky("0");
            
            List<String>  categoryList = item.getCvsTable().getSymboleWhereAccuracyOne();
            List<String>  niceNameList = item.getCvsTable().getSymboleWhereAccuracyOneLowerCase();
            List<String> domainList = new ArrayList();
            
            if(item.getPositionType().equalsIgnoreCase("Long & Short")){
                categoryList.addAll(item.getCvsTable2().getSymboleWhereAccuracyOne());
                niceNameList.addAll(item.getCvsTable2().getSymboleWhereAccuracyOneLowerCase());
            }
            
            for(int i = 0 ; i < categoryList.size() ; i++){
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
            RSSWriter.write(feed, RSSFEED);
            System.out.println("Done");
            
        }catch (FileNotFoundException e) {
            ErrorMessages.printErrorMsg(ErrorMessages.FILENOTFOUND,RSSFEED);
            e.printStackTrace();
        } catch (IOException e) {
            ErrorMessages.printErrorMsg(ErrorMessages.FILECOR,RSSFEED);
            e.printStackTrace();
        } catch (Exception e){
            ErrorMessages.printErrorMsg(ErrorMessages.FILECOR,RSSFEED);
            e.printStackTrace();
        }
        
    }
    
    
}
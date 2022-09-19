package Miscellaneous;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Zaryab on 11/13/2016.
 */
public class QueryResultItems {
    //---------------------------text---------------------------
    private int rank;
    private String url;
    private double score;
    private String title;
    private String snippet;
    private String pageContent;

    //-------------------------getters/setters------------------
    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public double getScore() {
        return score;
    }
    public void setScore(double score) {
        this.score = score;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSnippet() {return snippet;}
    public void setSnippet(String snippet) {this.snippet = snippet+"";}
    public String getPageContent() {return pageContent;}
    public void setPageContent(String pageContent) {this.pageContent = pageContent;}
    //--------------------------------------------------------
    public QueryResultItems(){
    }

    //------------------------------Images---------------------------------------
    private int pageIndex, position;
    private String alt;
    private String type;
    private String src;
    private double imgScore;
    private byte[] img;
    //---------------getter/setter------------------------------
    public int getPageIndex() {return pageIndex;}
    public void setPageIndex(int pageIndex) {this.pageIndex = pageIndex;}
    public int getPosition() {return position;}
    public void setPosition(int position) {this.position = position;}
    public String getType() {return type;}
    public void setType(String type) {this.type = type;}
    public String getAlt() {return alt;}
    public void setAlt(String alt) {this.alt = alt;}
    public String getSrc() {return src;}
    public void setSrc(String src) {this.src = src;}
    public byte[] getImg() {return img;}
    public void setImg(byte[] img) {this.img = img;}
    public double getImgScore() {return imgScore;}
    public void setImgScore(double imgScore) {this.imgScore = imgScore;}

    public void calculateImgScore(List<String> qTerms)
    {
        String text = getPageContent().toLowerCase(); // get page content
        if(position>text.length()){
            imgScore=0;
            return;
        }
        String vicinity=text.substring(position-256<0?0:position-256,
                position+256>text.length()?text.length():position+256); // windows size 256 characters not words
        List<String> vFeatures= new ArrayList<String>(Arrays.asList(vicinity.split(" "))); // vicinity to list
        double sum=0f; // for calculation
        int distinctTerms=0;// same
        double factor =1;

        for(String s:qTerms)
        {
            if (vFeatures.contains(s))
            {
                distinctTerms++;
                sum += Collections.frequency(vFeatures, s);
            }
            if(getSrc().contains(s)) {factor+=1;sum++;} //high if src has the keywords
            if(getTitle().contains(s)) {factor+=1;sum++;} //high if title has the keywords
            if(getUrl().contains(s)) {factor+=1;sum++;} //high if url has the keywords

        }


        if(sum==0 && distinctTerms==0)
        {
            imgScore=0;
        }
        else
        { // calculate image score
            double x = (sum*(distinctTerms/qTerms.size())*(vicinity.length()/text.length()));
            imgScore = (.5f * Math.exp(-.5f * x))*(.8f + (getScore()*factor) * (1-.8))/2;
            imgScore*=factor;
        }

    }
}

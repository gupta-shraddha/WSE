package metaSearch;

import java.util.Comparator;

/**
 * Created by gupta on 07.02.2017.
 */
public class ResultMeta {
    private String engineURL;
    private double colScore;
    int rank;
    String url;
    Double tf_idf;

    public ResultMeta(int rank, String url, double tf_idf) {
        this.rank=rank;
        this.url=url;
        this.tf_idf=tf_idf;
    }
    public Double getTf_idf() {
        return tf_idf;
    }
    public int getRank(){
        return rank;
    }
    public String getUrl(){
        return url;
    }

    public String getEngineURL() {
        return engineURL;
    }

    public void setEngineURL(String engineURL) {
        this.engineURL = engineURL;
    }

    public void setColScore(double colScore) {
        this.colScore = colScore;
    }

    public double getScore() {
        return (getTf_idf()+0.4*getTf_idf()*colScore)/1.4;
    }

    public static Comparator<ResultMeta> normalizedComparator(){
        return new Comparator<ResultMeta>() {
            @Override
            public int compare(ResultMeta o1, ResultMeta o2) {
                return ((Double)o2.getScore()).compareTo(o1.getScore());
            }
        };
    }

}

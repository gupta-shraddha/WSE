package Miscellaneous;

import Algos_Compute.Levenshtein;
import DBPostgres.DBConnection;
import DBPostgres.DBSetup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * Created by gupta on 31.01.2017.
 */
public class adInfo {
    String id;
    String url;
    String username;
    int remaining_click;
    double bugdet;
    double costPerClick = 0.2;
    String description;
    String image;
    String ngrams;
    int score = 0;
    public static final int ad_k = 3;

    public adInfo () {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getremaining_click() {
        return remaining_click;
    }

    public void setremaining_click(int remaining_click) {
        this.remaining_click = remaining_click;
    }

    public double getBugdet() {
        return bugdet;
    }

    public void setBugdet(double bugdet) {
        this.bugdet = bugdet;
    }

    public double getCostPerClick() {
        return costPerClick;
    }

    public void setCostPerClick(double costPerClick) {
        this.costPerClick = costPerClick;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNgrams() {
        return ngrams;
    }

    public void setNgrams(String ngrams) {
        this.ngrams = ngrams;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    //Fetching the ad from database according to query Terms.

    public static List<adInfo> fetchAds(String query) {
        List<adInfo> ads = new ArrayList<adInfo>();
        if (query.length() < 2)
            return ads;

        String sql = " SELECT * FROM " + DBSetup.T_Ad
                + " WHERE " + DBSetup.Col_n_grams + " iLIKE ANY " + getTerms(query)
                + " AND " + DBSetup.Col_click_left + " > 0";

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                adInfo item = new adInfo();
                item.setId(rs.getString(DBSetup.Col_ad_id));
                item.setDescription(rs.getString(DBSetup.Col_description));
                item.setUrl(rs.getString(DBSetup.Col_url));
                item.setImage(rs.getString(DBSetup.Col_ad_image));
                item.setNgrams(rs.getString(DBSetup.Col_n_grams));
                item.calculateScore(query);
                ads.add(item);
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Collections.sort(ads, new Comparator<adInfo>() {
            @Override
            public int compare(adInfo o1, adInfo o2) {
                return o2.getScore() - o1.getScore();
            }
        });

        if (ads.size()>4)
            while (ads.size()>4)
                ads.remove(ads.size()-1);
        return ads;
    }
    //Compare the each related terms of query for related ad keywords.
    private static String getTerms(String query) {
        StringBuilder builder = new StringBuilder();
        builder.append(" ('{");
        String[] terms = query.split(" ");

        for (String s : Arrays.asList(terms)) {
            builder.append("\"%");
            builder.append(s);
            builder.append("%\",");
        }
        builder.deleteCharAt(builder.length()-1);
        builder.append("}') ");
        return builder.toString();
    }

    public static boolean register(String username, String ngrams, String url, String description,
                            double budget, double costPerClick) {
        Connection con = DBConnection.getConnection();

        try {
            int remaining_click = (int)(budget/costPerClick);
            String sql = "insert into " + DBSetup.T_Ad + " ("
                    + DBSetup.Col_cust + ", "
                    + DBSetup.Col_n_grams + ", "
                    + DBSetup.Col_url + ", "
                    + DBSetup.Col_description + ","
                    + DBSetup.Col_click_left + ","
                    + DBSetup.Col_cost_per_click + ", "
                    + DBSetup.Col_budget +  ", "
                    + DBSetup.Col_ad_image + ")"
                    + " values ('" + username + "', '" + ngrams + "','"
                    + url + "','" + description + "'," + remaining_click + ","
                    + costPerClick + "," + budget + ",'') ";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.execute();
            con.commit();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean register(String username, String ngrams, String url, String description,
                            double budget, double costPerClick, String image) {
        Connection con = DBConnection.getConnection();
        try {
            int remaining_click = (int)(budget/costPerClick);
            String sql = "insert into " + DBSetup.T_Ad + " ("
                    + DBSetup.Col_cust + ", "
                    + DBSetup.Col_n_grams + ", "
                    + DBSetup.Col_url + ", "
                    + DBSetup.Col_description + ","
                    + DBSetup.Col_click_left + ","
                    + DBSetup.Col_cost_per_click + ", "
                    + DBSetup.Col_budget + ", "
                    + DBSetup.Col_ad_image + ")"
                    + " values ('" + username + "', '" + ngrams + "','"
                    + url + "','" + description + "'," + remaining_click + ","
                    + costPerClick + "," + budget + ",'" + image + "') ";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.execute();
            con.commit();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void calculateScore(String query) {
        String[] ngrams = getNgrams().split(" ");
        String[] terms = query.split(" ");

        for (int i = 0; i < ngrams.length; i++) {
            for (int j = 0; j < terms.length; j++) {
                if (ad_k > Levenshtein.lDistance(ngrams[i], terms[j])) {
                    score++;
                }
            }
        }
    }

    public static void adClickUpdate(String id) {
        Connection con = DBConnection.getConnection(true);
        try {
            String sql = "Update "  + DBSetup.T_Ad +
                    " SET " + DBSetup.Col_click_left + " = " + DBSetup.Col_click_left + " - 1" +
                    " WHERE " + DBSetup.Col_ad_id + " = " + id + ";";
            System.out.println(sql);
            PreparedStatement ps = con.prepareStatement(sql);
            ps.execute();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        register("engadget", "mobile computer game news technology", "http://www.engadget.com/", "Engadget | Technology News, Advice and Features", 20, 0.02);
    }
}

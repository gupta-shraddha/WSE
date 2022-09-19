package DBPostgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Zaryab on 11/3/2016.
 */
public class DBSetup {

    public static Connection conn=null;
//-------------------------------------------------------------------------------------------------
    //Schema Definition
    //Table Names -> T_
    //Column Names -> Col_
    //Function Names -> Func_
    //View names -> View_
    //Index name -> I__

    //---------Tables---------//
    public static final String T_Documents = "documents";
    public static final String T_Features = "features";
    public static final String T_Links = "links";
    public static final String T_RootsQueue = "crawl_roots"; // stores initial URLS
    public static final String T_CrawlQueue = "crawl_queue";   // stores all external & sub-external urls
    public static final String T_noOfDocs = "doc_count";
    public static final String T_Images = "images";
    public static final String T_Shingles = "shingles";
    public static final String T_Jaccard = "jaccard";
    public static final String T_MinCnfgShingles = "min_cnfg_shingles";
    public static final String T_MetaSearch ="meta_search_engine";
    public static final String T_MetaSearch_terms ="meta_engine_terms";
    public static final String T_Ad = "ads";
    //-------Functions-------//
    public static final String Func_CalcIDF = "compute_idf";
    public static final String Func_CalcTFIDF = "compute_tfidf";
    public static final String Func_CalcBM25 = "compute_bm25";
    public static final String Func_DocCount = "document_count"; // total documents containing term for tfidf computation
    public static final String Funct_DocTermCount = "document_term_count";
    public static final String Func_AvgDocLength = "avg_doc_length";
    public static final String Func_DocLength = "doc_length";
    public static final String Func_JaccardDuplicates = "jaccard_duplicates";
    public static final String Func_md5_to_bigint ="txt_md5_int";
    public static final String Func_active_engine = "engine_count";
    public static final String Func_avg_cw = "avg_cw";
    public static final String Func_cf = "cf";
    //------Views-----------//
    public static final String View_Features_tfidf = "features_tfidf";
    public static final String View_Features_bm25 = "features_bm25";
    public static final String View_bm25_pagerank = "bm25_pagerank";
    public static final String View_DocumentsPairs = "documents_pairs";
    //------Index----------//
    public static final String I__T_documents_docId = "documents_docID";
    public static final String I__T_documents_url = "documents_Url";
    public static final String I__T_features_docId = "features_docID";
    public static final String I__T_features_termId = "features_termID";
    public static final String I__T_features_term = "features_term";
    public static final String I__T_Shingles_shingleHash = "shingles_shingleHash";
    public static final String I__T_features_language = "features_language";

    //-------Columns--------//
    //T_Documents
    public static final String Col_docId = "doc_id";
    public static final String Col_url = "url";
    public static final String Col_crawledOnDate = "crawled_on_date";
    public static final String Col_content = "content";
    public static final String Col_pageTitle = "title";
    public static final String Col_pageDescription = "description"; //
    public static final String Col_pageKeywords = "keywords";//
    public static final String Col_pageRank = "pagerank";
    public static final String Col_length = "length";
    //T_Features
    public static final String Col_term = "term";
    public static final String Col_termFrequency = "term_frequency";
    public static final String Col_termId = "term_Id";
    public static final String Col_idf = "idf";
    public static final String Col_tfIdf = "tfIdf";
    public static final String Col_bm25 = "bm25";
    public static final String Col_idfBM25 = "idfBM25";
    public static final String Col_bm25_pagerank = "bm25_pagerank";
    public static final String Col_language = "language";
    //T_Shingle
    public static final String Col_shingle = "shingle";
    public static final String Col_shingleHash = "shingle_hash";
    //Table Jaccard
    public static final String Col_doc1 = "doc1";
    public static final String Col_doc2 = "doc2";
    public static final String Col_jaccardValue = "jaccard";
    // T_Links
    public static final String Col_fromDocId = "from_docid";
    public static final String Col_toDocId = "to_docid";
    //T_RootsQueue
    public static final String Col_leaveDomain = "leave_domain";
    public static final String Col_maxDoc = "max_doc";
    //T_CrawlQueue
    public static final String Col_depth = "depth";
    public static final String Col_visited = "visited";
    //T_noOfDocs
    public static final String Col_count = "count";
    //T_Images
    public static final String Col_image = "image";
    public static final String Col_alt = "alt";
    public static final String Col_src = "src";
    public static final String Col_type = "type";
    public static final String Col_index = "index";
    public static final String Col_position = "position";
    //T_MetaSearch
    public static final String Col_id = "id";
    public static final String Col_active = "active";
    public static final String Col_df = "df";
    //T_MetaSearch_Terms
    public static final String Col_cw = "cw";
    public static final String Col_hash = "hash";
    //T_Ad
    public static final String Col_ad_id = "ad_id";
    public static final String Col_cust = "users";
    public static final String Col_n_grams = "ngrams";
    public static final String Col_description = "description";
    public static final String Col_budget = "budget";
    public static final String Col_click_left = "remaining_click";
    public static final String Col_cost_per_click = "cost_per_click";
    public static final String Col_ad_image = "image";
    //-------------------------------------------------------------------------------------------------
    public static void createTables() {
        System.out.println(T_Documents.toUpperCase()+" Table exist : "+ createT_Documents(false));
        System.out.println(T_Features.toUpperCase()+" Table exist : "+createT_Features(false));
        System.out.println(T_Links.toUpperCase()+" Table exist : "+ createT_Links(false));
        System.out.println(T_RootsQueue.toUpperCase()+" Table exist : "+createT_RootsQueue(false));
        System.out.println(T_CrawlQueue.toUpperCase() +" Table exist : "+ createT_CrawlQueue(false));
        System.out.println(T_Images.toUpperCase() +" Table exist : "+ createT_Images(false));
        System.out.println(T_Shingles.toUpperCase() +" Table exist : "+ createT_Shingles(false));
        System.out.println(T_Jaccard.toUpperCase() +" Table exist : "+createT_Jaccard(false));
        System.out.println(T_MinCnfgShingles.toUpperCase() +" Table exist : "+createT_MinCnfgShingles(false));
        System.out.println(T_noOfDocs.toUpperCase()+" Table exist : "+ createT_noOfDocs(false));
        System.out.println(T_MetaSearch.toUpperCase()+" Table exist : "+ createT_MetaSearch(false));
        System.out.println(T_MetaSearch_terms.toUpperCase()+" Table exist : "+ createT_MetaSearch_Terms(false));
        System.out.println(T_Ad.toUpperCase()+"Table exist : " + createT_Ads(true));

    }
    //-------------------------------------------------------------------------------------------------
    public static void createFunctions() {
        System.out.println(Func_DocCount.toUpperCase()+" Function exist : "+ calculateFunc_DocCount(false));
        System.out.println(Funct_DocTermCount.toUpperCase()+" Function exist : "+ calculateFunc_DocTermCount(false));
        System.out.println(Func_CalcIDF.toUpperCase()+" Function exist : " + calculateFunc_IDF(false));
        System.out.println(Func_CalcTFIDF.toUpperCase()+" Function exist : " + calculateFunc_TFIDF(false));
        System.out.println(Func_DocLength.toUpperCase()+" Function exist : " + fetchFunc_DocLength(false));
        System.out.println(Func_AvgDocLength.toUpperCase()+" Function exist : " + fetchFunc_AvgDocLength(false));
        System.out.println(Func_JaccardDuplicates.toUpperCase()+" Function exist : " + Func_JaccardDuplicates(false));
        System.out.println(Func_md5_to_bigint.toUpperCase()+" Function exist : " + Func_Md5ToBigInt(false));
        System.out.println(Func_CalcBM25.toUpperCase()+" Function exist : " +calculateFunc_bm25(false));
        System.out.println(Func_active_engine.toUpperCase()+" Function exist : " + engine_count(false));
        System.out.println(Func_cf.toUpperCase()+" Function exist : " + cf(false));
        System.out.println(Func_avg_cw.toUpperCase()+" Function exist: " + avg_cw(true));

    }
    //-------------------------------------------------------------------------------------------------
    public static void createViews(){
        System.out.println(View_Features_tfidf.toUpperCase()+" View exist : "+ createView_FeaturesTFIDF(false));
        System.out.println(View_Features_bm25.toUpperCase()+" View exist : "+ createView_FeaturesBm25(false));
        System.out.println(View_DocumentsPairs.toUpperCase()+" View exist : "+ createView_DocumentsPairs(false));
        System.out.println(View_bm25_pagerank.toUpperCase()+" View exist : "+ createView_Bm25Pagerank(true));
    }
    //--------------------------------------------------------------------------------------------------
    public static void createIndexes() {
        System.out.println(I__T_documents_docId.toUpperCase()+" Index created :  " + createDocumentsDocIdIndex(false));
        System.out.println(I__T_documents_url.toUpperCase()+" Index created : " + createDocumentsUrlIndex(false));
        System.out.println(I__T_features_docId.toUpperCase()+" Index created : " + createFeaturesDocIdIndex(false));
        System.out.println(I__T_features_language.toUpperCase()+" Index created : " + createFeaturesLanguageIndex(false));
        System.out.println(I__T_features_termId.toUpperCase()+" Index created : " + createFeaturesTermIdIndex(false));
        System.out.println(I__T_Shingles_shingleHash.toUpperCase()+" Index created : " + createShinglesShingleHashIndex(false));
        System.out.println(I__T_features_term.toUpperCase()+" Index created : " + createFeaturesTermIndex(true));
    }

    // -------------------------------------------------------------------------------------------------
    private static boolean createT_Features(boolean closeDb) {
        return createTable("CREATE TABLE IF NOT EXISTS "+T_Features+" ("
                + Col_docId +" Integer, "            + Col_term + " Varchar(255) NOT NULL, "
                + Col_termId + " Integer NOT NULL, " + Col_bm25 + " double precision DEFAULT 0.0, "
                + Col_idf + " double precision, "    + Col_tfIdf + " double precision DEFAULT 0.0, "
                + Col_bm25_pagerank + " double precision DEFAULT 0.0, " + Col_idfBM25 + " double precision DEFAULT 0.0, "
                + Col_language + " varchar(2) check ( " + Col_language + " IN ('en','de')), " + Col_pageRank + " double precision,"
                + Col_termFrequency + " Integer,"    + "CONSTRAINT featuresPK PRIMARY KEY("+Col_docId+","+Col_termId+"))",closeDb);
    }

    private static boolean createT_Documents(boolean closeDb) {
        return createTable("CREATE TABLE IF NOT EXISTS "+T_Documents+" ("
                + Col_docId + " INTEGER PRIMARY KEY, "     + Col_url + " Varchar(255) Unique, "
                + Col_content + " text, "                  + Col_pageDescription + " text, "
                + Col_pageKeywords + " text, "             + Col_pageRank + " double precision,"
                + Col_pageTitle + " varchar(255), "        + Col_length + " INTEGER, "
                + Col_language + " varchar(2) check ( " + Col_language + " IN ('en','de')), "
                + Col_crawledOnDate + " Timestamp DEFAULT ('now'::text)::Timestamp );",closeDb);
    }

    private static boolean createT_Links(boolean closeDb) {
        return createTable("CREATE TABLE IF NOT EXISTS " + T_Links + " ("
                + Col_fromDocId + " Integer, "        + Col_toDocId +" Integer, " +
                "CONSTRAINT links_PK PRIMARY KEY("+Col_fromDocId+","+Col_toDocId+"));",closeDb);
    }
    private static boolean createT_RootsQueue(boolean closeDb) {
        return createTable("CREATE TABLE IF NOT EXISTS " + T_RootsQueue + " ( "
                + Col_docId + " INTEGER PRIMARY KEY, "        + Col_url + " Varchar(255) UNIQUE, "
                + Col_depth +" Integer, "           + Col_maxDoc +" Integer, "
                + Col_leaveDomain + " Boolean, "    + Col_crawledOnDate + " Timestamp DEFAULT ('now'::text)::Timestamp, "
                + Col_visited +" Boolean DEFAULT False)", closeDb);
    }
    private static boolean createT_CrawlQueue(boolean closeDb) {
        return createTable("CREATE TABLE IF NOT EXISTS " + T_CrawlQueue + " ( "
                + Col_docId + " INTEGER PRIMARY KEY, "        + Col_url + " varchar(255) UNIQUE, "
                + Col_depth +" INTEGER, "        + Col_visited +" BOOLEAN DEFAULT FALSE)", closeDb);
    }
    private static boolean createT_noOfDocs(boolean closeDb){
        return createTable("CREATE TABLE IF NOT EXISTS " +
                T_noOfDocs + "("+ Col_termId + " int PRIMARY key, " + Col_count+ " int);",closeDb);
    }
    private static boolean createT_Images(boolean closeDb) {
        return createTable("CREATE TABLE IF NOT EXISTS "+T_Images+" ("
                + Col_docId +" INTEGER, "
                + Col_index +" INTEGER, "
                + Col_src + " text, "
                + Col_alt + " text, "
                + Col_type + " varchar, "
                + Col_image + " bytea, "
                + Col_position + " INTEGER DEFAULT -1, "
                + "CONSTRAINT imagePK PRIMARY KEY("+Col_docId+","+Col_index+"))",closeDb);
    }
    private static boolean createT_Shingles(boolean closeDb) {
        return createTable("CREATE TABLE IF NOT EXISTS " + T_Shingles + " ( "
                + Col_docId + " INTEGER, "+ Col_shingle + " varchar, "+ Col_shingleHash +" bigint, "
                +"CONSTRAINT Shingles_PK PRIMARY KEY("+Col_docId+","+Col_shingleHash+"))", closeDb);
    }
    private static boolean createT_MinCnfgShingles(boolean closeDb) {
        return createTable("CREATE TABLE IF NOT EXISTS " + T_MinCnfgShingles + " ( "
                + Col_docId + " INTEGER, "+ Col_shingleHash +" bigint, "
                +"CONSTRAINT MinShingles_PK PRIMARY KEY("+Col_docId+","+Col_shingleHash+"))", closeDb);
    }
    private static boolean createT_Jaccard(boolean closeDb){
        return createTable("CREATE TABLE IF NOT EXISTS " + T_Jaccard + "("
                + Col_doc1 + " int , " + Col_doc2+ " int," + Col_jaccardValue + " double precision , " +
                "jaccard1 double precision, jaccard4 double precision, jaccard16 double precision,jaccard32 double precision,"
                + "CONSTRAINT Jaccard_PK PRIMARY KEY("+Col_doc1+","+Col_doc2+"))",closeDb);
    }
    private static boolean createT_MetaSearch(boolean closeDb) {
        return createTable("CREATE TABLE IF NOT EXISTS " + T_MetaSearch + " ( "
                + Col_id + " SERIAL, "
                + Col_url + " varchar, "
                + Col_active + " BOOLEAN,"
                + Col_cw + " INTEGER DEFAULT -1)", closeDb);
    }
    private static boolean createT_MetaSearch_Terms(boolean closeDb) {
        return createTable("CREATE TABLE IF NOT EXISTS " + T_MetaSearch_terms + " ( "
                + Col_id + " INTEGER , "
                + Col_hash + " INTEGER, "
                + Col_df + " INTEGER , "
                +"CONSTRAINT engineTerms PRIMARY KEY("+Col_id+","+Col_hash+"))",closeDb);
    }

    private static boolean createT_Ads(boolean closeDb) {
        return createTable("CREATE TABLE IF NOT EXISTS " + T_Ad + " ( "
                + Col_ad_id + " serial primary key, "
                + Col_cust + " text, "
                + Col_n_grams + " text, "
                + Col_url + " text, "
                + Col_description + " text, "
                + Col_ad_image + " text, "
                + Col_click_left + " INTEGER, "
                + Col_cost_per_click + " NUMERIC, "
                + Col_budget + " NUMERIC) " , closeDb);
    }
    //---------------------------------------------------------------------------------------
    private static boolean calculateFunc_IDF(boolean closeDb){ // termid in count documents
        String N= "cast("+Func_DocCount+"() as real)";    // total documents
        String n="cast(	(SELECT " + Col_count + " " +     //total documents containing terms.
                "FROM " + T_noOfDocs + " WHERE " + T_Features + "." + Col_termId + "= " + T_noOfDocs + "." + Col_termId + ") as real) ";
        return createFunction("CREATE OR REPLACE FUNCTION "+Func_CalcIDF+"() RETURNS BOOLEAN LANGUAGE SQL " +
                "AS  $$ " +
                "TRUNCATE " + T_noOfDocs + "; " +
                "INSERT INTO " + T_noOfDocs + " " +
                "SELECT " + Col_termId + ", COUNT(" + Col_docId + ") as " + Col_count + " from " + T_Features + " " +
                "GROUP BY " + Col_termId + "; "+
                "UPDATE " + T_Features + " SET " + Col_idf +" = " +
                "log( " +N+ "/abs("+ n +")),"+
                Col_idfBM25 +" = " + "log( (" +N+"-"+n +"+0.5) /("+n+"+0.5) ) "+
                "RETURNING TRUE; $$",closeDb);
    }
    private static boolean calculateFunc_DocCount(boolean closeDb){
        return createFunction("CREATE OR REPLACE FUNCTION "+Func_DocCount+"() RETURNS bigint LANGUAGE SQL " +
                "AS $$ SELECT COUNT(" + T_Documents +"." + Col_docId + ") " +
                "FROM " + T_Documents + "; $$ ", closeDb);
    }
    private static boolean calculateFunc_TFIDF(boolean closeDb){
        return createFunction("CREATE OR REPLACE FUNCTION "+Func_CalcTFIDF+"() RETURNS BOOLEAN LANGUAGE SQL " +
                "AS $$ UPDATE " + T_Features +" SET " + Col_tfIdf +" = " +
                "(1+log("+Col_termFrequency+"))*"+Col_idf
                + " RETURNING TRUE; $$", closeDb);
    }
    private static boolean calculateFunc_bm25(boolean closeDb){
        double b=.75,k1=1.6; // k between 1.2-2.0
        return createFunction("CREATE OR REPLACE FUNCTION "+Func_CalcBM25+"() RETURNS BOOLEAN LANGUAGE SQL " +
                "AS $$ UPDATE " + T_Features +" SET " + Col_bm25 +" = "
                +Col_idfBM25 + "*(" +
                "(" + Col_termFrequency + "*("+ k1 +"+1))"
                + "/" +
                Col_termFrequency + "+"+ k1 +"*(1-" + b + "+" + b + "*(" + Func_DocLength + "("+Col_docId+")/" + Func_AvgDocLength +"()))" +
                ")"
                + " RETURNING TRUE; $$", closeDb);
    }
    private static boolean calculateFunc_DocTermCount(boolean closeDb){
        return createFunction("CREATE OR REPLACE FUNCTION "+Funct_DocTermCount+"(term_Id int) RETURNS bigint LANGUAGE SQL STABLE " +
                "AS $$ SELECT COUNT(" + Col_docId + ") FROM " + T_Features + " " +
                "WHERE " + Col_termId +" = term_id GROUP BY " + Col_termId +" $$",closeDb);
    }
    private static boolean fetchFunc_DocLength(boolean closeDb){
        return createFunction("CREATE OR REPLACE FUNCTION "+Func_DocLength+"(doc int) RETURNS int LANGUAGE SQL " +
                "AS $$ SELECT "+ Col_length +" FROM " + T_Documents + " WHERE " + Col_docId +"=doc; $$",closeDb);
    }

    private static boolean fetchFunc_AvgDocLength(boolean closeDb){
        return createFunction("CREATE OR REPLACE FUNCTION "+Func_AvgDocLength+"() RETURNS numeric LANGUAGE SQL STABLE " +
                "AS $$ SELECT "+ "avg("+Col_length+")" + " FROM " + T_Documents +  "; $$",closeDb);
    }
    private static boolean Func_JaccardDuplicates(boolean closeDb){
        return createFunction("CREATE OR REPLACE FUNCTION "+ Func_JaccardDuplicates+"(docID integer, threshhold double precision)"+
                " RETURNS TABLE (d1 int,d2 int,jaccard double precision) AS $$ "+
                " SELECT doc1 ,doc2 ,"+Col_jaccardValue+" FROM "+T_Jaccard+" WHERE doc1 = docID And "+Col_jaccardValue+">=threshhold "+
                "$$ LANGUAGE 'sql' ;",closeDb);
    }
    private static boolean Func_Md5ToBigInt(boolean closeDb){
        return createFunction("Create or Replace Function "+ Func_md5_to_bigint +"(text) Returns bigint as $$" +
                " SELECT ('x'||substr(md5($1),1,16))::bit(64)::bigint; $$ Language sql;",closeDb);
        //converts first 32 bits of md5 hex into int
    }
    private static boolean avg_cw(boolean closeDb){
        return createTable("CREATE OR REPLACE FUNCTION "+Func_avg_cw+"() RETURNS NUMERIC LANGUAGE SQL STABLE " +
                "AS $$ SELECT AVG("+Col_cw+")" +
                " FROM " + T_MetaSearch +  "; $$",closeDb);
    }

    private static boolean engine_count(boolean closeDb){
        return createTable("CREATE OR REPLACE FUNCTION "+Func_active_engine+"() RETURNS BIGINT LANGUAGE SQL STABLE " +
                "AS $$ SELECT COUNT(*)" +
                " FROM " + T_MetaSearch +  "; $$",closeDb);
    }

    private static boolean cf(boolean closeDb){
        return createTable("CREATE OR REPLACE FUNCTION "+Func_cf+"(term_id int) RETURNS NUMERIC LANGUAGE SQL STABLE " +
                "AS $$ SELECT (1.0*engine_count())/COUNT(*)" +
                " FROM " + T_MetaSearch_terms  +  " WHERE "+DBSetup.Col_hash+"=term_id; $$",closeDb);
    }


    //--------------------------------------------------------------------------------------------
    private static boolean createDocumentsDocIdIndex(boolean closeDb){
        return createIndex("CREATE INDEX "+I__T_documents_docId+" ON " + T_Documents+
                " USING hash("+Col_docId +") ",closeDb);
    }

    private static boolean createDocumentsUrlIndex(boolean closeDb){
        return createIndex("CREATE INDEX "+I__T_documents_url+" ON " + T_Documents+
                " ("+Col_url +") ",closeDb);
    }

    private static boolean createFeaturesDocIdIndex(boolean closeDb){
        return createIndex("CREATE INDEX "+I__T_features_docId+" ON " + T_Features+
                " USING hash("+Col_docId +") ", closeDb);
    }

    private static boolean createFeaturesTermIdIndex(boolean closeDb){
        return createIndex("CREATE INDEX "+I__T_features_termId+" ON " + T_Features+
                " USING hash("+Col_termId +") ",closeDb);
    }
    private static boolean createFeaturesLanguageIndex(boolean closeDb){
        return createIndex("CREATE INDEX "+ I__T_features_language + " ON " + T_Features + " " +
                "USING hash ( " + Col_language +") ", closeDb);
    }
    private static boolean createFeaturesTermIndex(boolean closeDb){
        return createIndex("CREATE INDEX "+ I__T_features_term + " ON " + T_Features + " " +
                "USING hash ( " + Col_term +") ", closeDb);
    }
    private static boolean createShinglesShingleHashIndex(boolean closeDb){
        return createIndex("CREATE INDEX "+I__T_Shingles_shingleHash+" ON " + T_Shingles + " " +
                "Using hash ( " + Col_shingleHash +") ", closeDb);
    }

    //--------------------------------------------------------------------------------------------
    private static boolean createView_FeaturesTFIDF(boolean closeDb) {
        return createView("CREATE OR REPLACE VIEW " + View_Features_tfidf
                + " AS SELECT "
                + Col_docId +", "
                + Col_term + ", "
                + Col_termId + ", "
                + Col_termFrequency + ", "
                + Col_tfIdf + " AS score"
                + " FROM " + T_Features, closeDb);
    }
    private static boolean createView_FeaturesBm25(boolean closeDb) {
        return createView("CREATE OR REPLACE VIEW " + View_Features_bm25
                + " AS SELECT "
                + Col_docId +", "
                + Col_term + ", "
                + Col_termId + ", "
                + Col_termFrequency + ", "
                + Col_bm25 + " AS score"
                + " FROM " + T_Features, closeDb);
    }
    private static boolean createView_Bm25Pagerank(boolean closeDb) {
        return createView("CREATE OR REPLACE VIEW " + View_bm25_pagerank
                + " AS SELECT "
                + Col_docId +", "
                + Col_term + ", "
                + Col_termId + ", "
                + Col_termFrequency + ", "
                + Col_bm25_pagerank + " AS score"
                + " FROM " + T_Features , closeDb);
    }
    private static boolean createView_DocumentsPairs(boolean closeDb) {
        return createTable("CREATE OR REPLACE VIEW "+View_DocumentsPairs
                +" AS SELECT d1."+Col_docId+" AS doc1, d2."+Col_docId+" AS doc2 FROM "+T_Documents +" d1 CROSS JOIN "+T_Documents+" d2 WHERE d1."+Col_docId+" <> d2."+Col_docId, closeDb);
    }
    //--------------------------------------------------------------------------------------------
    private static boolean createTable (String sql, boolean closeDb)
    {
        return executeQuery(sql,true, closeDb);
    }
    private static boolean createFunction (String sql, boolean closeDb)
    {
        return executeQuery(sql,true, closeDb);
    }
    private static boolean createView (String sql, boolean closeDb)
    {
        return executeQuery(sql,true, closeDb);
    }
    private static boolean createIndex (String sql, boolean closeDb)
    {
        return executeQuery(sql,false, closeDb);
    }
    //--------------------------------------------------------------------------------------------
    private static boolean DropSchema()
    {
        String Sql="DROP SCHEMA public CASCADE;\n" +
                "CREATE SCHEMA public;\n" +
                "GRANT ALL ON SCHEMA public TO postgres;\n" +
                "GRANT ALL ON SCHEMA public TO public;\n" +
                "COMMENT ON SCHEMA public IS 'standard public schema'";
        return executeQuery(Sql, true, true); // close connection;
    }
    public static boolean fuzzyExtension()
    {
        String Sql="Create EXTENSION IF NOT EXISTS fuzzystrmatch;";
        return executeQuery(Sql, true, true); // close connection;
    }

    private static boolean executeQuery(String sql, boolean printException, boolean closeDb)
    {
        try {
            if (conn == null )
            {conn = DBConnection.getConnection();}
            if( conn.isClosed())
            {conn = DBConnection.getConnection();}
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            conn.commit();
            return true;
        } catch (SQLException e) {
            if(printException)
                e.printStackTrace();
        }
        finally {
            if(conn!= null && closeDb)
                try {
                    conn.close();
                } catch (SQLException e) {}
        }
        return false;
    }
    public static void SetupDB() throws SQLException {
        System.out.println("Schema Dropped : "+DropSchema());
        createTables();
        createFunctions();
        createViews();
        //endCrawler(DBConnection.getConnection());

    }
    public static void main (String[] args) throws SQLException
    {
        //SetupDB();
    }
    public static void endCrawler(Connection con) throws SQLException
    {
        try {
            Statement stmt = con.createStatement();
            stmt.execute("DROP SEQUENCE docs_Crawled ");
            // stmt.execute("TRUNCATE docs_Crawled RESTART IDENTITY ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.close();
    }


}

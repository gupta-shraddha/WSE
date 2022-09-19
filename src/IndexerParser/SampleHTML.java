package IndexerParser;

/**
 * Created by Zaryab on 11/12/2016.
 */
public class SampleHTML {


    public String getStringHTML() {
        return StringHTML;
    }

    public void setStringHTML(String stringHTML) {
        StringHTML = stringHTML;
    }

    public String StringHTML=null;

    public SampleHTML()
    {
     StringHTML="\n" +
             " <!DOCTYPE html>\n" +
             "<!--[if lt IE 7]>      <html class=\"no-js lt-ie9 lt-ie8 lt-ie7\"  lang=\"en-gb\"> <![endif]-->\n" +
             "<!--[if IE 7]>         <html class=\"no-js lt-ie9 lt-ie8\"  lang=\"en-gb\"> <![endif]-->\n" +
             "<!--[if IE 8]>         <html class=\"no-js lt-ie9\"  lang=\"en-gb\"> <![endif]-->\n" +
             "<!--[if gt IE 8]><!--> <html class=\"no-js\" lang=\"en-gb\"> <!--<![endif]-->\n" +
             "    <head>\n" +
             "        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
             "          <base href=\"http://dbis.informatik.uni-kl.de/index.php/en/people/evica-milchevski\" />\n" +
             "  <meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\n" +
             "  <meta name=\"author\" content=\"Super User\" />\n" +
             "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
             "  <meta name=\"description\" content=\"DBIS - Arbeitsgruppe Datenbanken und Informationssysteme / Databases and Information Systems Group, Fachbereich Informatik / Computer Science Department, Technische Universitaet (TU) Kaiserslautern, Deutschland / Germany, Prof. Sebastian Michel\" />\n" +
             "  <title>Evica Milchevski</title>\n" +
             "  <link href=\"/templates/lt_start_up_onepage/favicon.ico\" rel=\"shortcut icon\" type=\"image/vnd.microsoft.icon\" />\n" +
             "  <link rel=\"stylesheet\" href=\"/templates/lt_start_up_onepage/css/bootstrap.min.css\" type=\"text/css\" />\n" +
             "  <link rel=\"stylesheet\" href=\"/templates/lt_start_up_onepage/css/bootstrap-responsive.min.css\" type=\"text/css\" />\n" +
             "  <link rel=\"stylesheet\" href=\"/plugins/system/helix/css/font-awesome.css\" type=\"text/css\" />\n" +
             "  <link rel=\"stylesheet\" href=\"/templates/lt_start_up_onepage/css/mobile-menu.css\" type=\"text/css\" />\n" +
             "  <link rel=\"stylesheet\" href=\"/templates/lt_start_up_onepage/css/template.css\" type=\"text/css\" />\n" +
             "  <link rel=\"stylesheet\" href=\"/templates/lt_start_up_onepage/css/presets/preset1.css\" type=\"text/css\" />\n" +
             "  <style type=\"text/css\">\n" +
             ".container{max-width:1170px}\n" +
             "#sp-main-body-wrapper{background: rgba(246, 180, 74, 0) !important; padding: 30px 0 !important; }\n" +
             "\n" +
             "#sp-main-body-wrapper{background: rgba(246, 180, 74, 0) !important; padding: 30px 0 !important; }\n" +
             "\n" +
             "  </style>\n" +
             "  <script src=\"/media/jui/js/jquery.min.js\" type=\"text/javascript\"></script>\n" +
             "  <script src=\"/media/jui/js/jquery-noconflict.js\" type=\"text/javascript\"></script>\n" +
             "  <script src=\"/media/jui/js/jquery-migrate.min.js\" type=\"text/javascript\"></script>\n" +
             "  <script src=\"/plugins/system/helix/js/jquery-noconflict.js\" type=\"text/javascript\"></script>\n" +
             "  <script src=\"/media/jui/js/bootstrap.min.js\" type=\"text/javascript\"></script>\n" +
             "  <script src=\"/plugins/system/helix/js/modernizr-2.6.2.min.js\" type=\"text/javascript\"></script>\n" +
             "  <script src=\"/plugins/system/helix/js/helix.core.js\" type=\"text/javascript\"></script>\n" +
             "  <script src=\"/plugins/system/helix/js/menu.js\" type=\"text/javascript\"></script>\n" +
             "  <script src=\"/templates/lt_start_up_onepage/js/fixed-menu.js\" type=\"text/javascript\"></script>\n" +
             "  <script src=\"/templates/lt_start_up_onepage/js/scroll.js\" type=\"text/javascript\"></script>\n" +
             "  <script type=\"text/javascript\">\n" +
             "spnoConflict(function($){\n" +
             "\n" +
             "\t\t\t\t\tfunction mainmenu() {\n" +
             "\t\t\t\t\t\t$('.sp-menu').spmenu({\n" +
             "\t\t\t\t\t\t\tstartLevel: 0,\n" +
             "\t\t\t\t\t\t\tdirection: 'ltr',\n" +
             "\t\t\t\t\t\t\tinitOffset: {\n" +
             "\t\t\t\t\t\t\t\tx: 0,\n" +
             "\t\t\t\t\t\t\t\ty: 0\n" +
             "\t\t\t\t\t\t\t},\n" +
             "\t\t\t\t\t\t\tsubOffset: {\n" +
             "\t\t\t\t\t\t\t\tx: 0,\n" +
             "\t\t\t\t\t\t\t\ty: 0\n" +
             "\t\t\t\t\t\t\t},\n" +
             "\t\t\t\t\t\t\tcenter: 0\n" +
             "\t\t\t\t\t\t});\n" +
             "\t\t\t}\n" +
             "\n" +
             "\t\t\tmainmenu();\n" +
             "\n" +
             "\t\t\t$(window).on('resize',function(){\n" +
             "\t\t\t\tmainmenu();\n" +
             "\t\t\t});\n" +
             "\n" +
             "\n" +
             "\t\t\t});\n" +
             "  </script>\n" +
             "\n" +
             "            \n" +
             "\n" +
             "\n" +
             "</head>\n" +
             "    <body  class=\"article subpage  ltr preset1 menu-evica-milchevski responsive bg hfeed clearfix\">\n" +
             "\t\t<div class=\"body-innerwrapper\">\n" +
             "        <!--[if lt IE 8]>\n" +
             "        <div class=\"chromeframe alert alert-danger\" style=\"text-align:center\">You are using an <strong>outdated</strong> browser. Please <a target=\"_blank\" href=\"http://browsehappy.com/\">upgrade your browser</a> or <a target=\"_blank\" href=\"http://www.google.com/chromeframe/?redirect=true\">activate Google Chrome Frame</a> to improve your experience.</div>\n" +
             "        <![endif]-->\n" +
             "        <header id=\"sp-header-wrapper\" \n" +
             "                class=\" \"><div class=\"container\"><div class=\"row-fluid\" id=\"header\">\n" +
             "<div id=\"sp-logo\" class=\"span12\"><div class=\"logo-wrapper\"><a href=\"/\"><img alt=\"\" class=\"image-logo\" src=\"/images/own_tukl_logo.png\" /></a></div></div>\n" +
             "</div></div></header><section id=\"sp--wrapper\" \n" +
             "                class=\" \"><div class=\"row-fluid\" id=\"\">\n" +
             "<div id=\"sp-menu\" class=\"span12\">\t\n" +
             "\n" +
             "\n" +
             "\t\t\t<div id=\"sp-main-menu\" class=\"visible-desktop\">\n" +
             "\t\t\t\t<ul class=\"sp-menu level-0\"><li class=\"menu-item first\"><a href=\"http://dbis.informatik.uni-kl.de/\" class=\"menu-item first\" ><span class=\"menu\"><span class=\"menu-title\">Home</span></span></a></li><li class=\"menu-item active parent \"><a href=\"/index.php/en/people\" class=\"menu-item active parent \" ><span class=\"menu\"><span class=\"menu-title\">People</span></span></a><div class=\"sp-submenu\"><div class=\"sp-submenu-wrap\"><div class=\"sp-submenu-inner clearfix\" style=\"width: 200px;\"><div class=\"megacol col1 first\" style=\"width: 200px;\"><ul class=\"sp-menu level-1\"><li class=\"menu-item first\"><a href=\"/index.php/en/people/michel\" class=\"menu-item first\" ><span class=\"menu\"><span class=\"menu-title\">Prof. Sebastian Michel</span></span></a></li><li class=\"menu-item\"><a href=\"/index.php/en/people/heike-neu\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">Heike Neu</span></span></a></li><li class=\"menu-item\"><a href=\"/index.php/en/people/steffen-reithermann\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">Steffen Reithermann</span></span></a></li><li class=\"menu-item\"><a href=\"/index.php/en/people/manuel-hoffmann\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">Manuel Hoffmann</span></span></a></li><li class=\"menu-item active\"><a href=\"/index.php/en/people/evica-milchevski\" class=\"menu-item active\" ><span class=\"menu\"><span class=\"menu-title\">Evica Milchevski</span></span></a></li><li class=\"menu-item\"><a href=\"/index.php/en/people/kiril-panev\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">Kiril Panev</span></span></a></li><li class=\"menu-item\"><a href=\"/index.php/en/people/koninika-pal\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">Koninika Pal</span></span></a></li><li class=\"menu-item last\"><a href=\"/index.php/en/people/alumni\" class=\"menu-item last\" ><span class=\"menu\"><span class=\"menu-title\">Alumni</span></span></a></li></ul></div></div></div></div></li><li class=\"menu-item parent \"><a href=\"/index.php/en/teaching\" class=\"menu-item parent \" ><span class=\"menu\"><span class=\"menu-title\">Teaching</span></span></a><div class=\"sp-submenu\"><div class=\"sp-submenu-wrap\"><div class=\"sp-submenu-inner clearfix\" style=\"width: 200px;\"><div class=\"megacol col1 first\" style=\"width: 200px;\"><ul class=\"sp-menu level-1\"><li class=\"menu-item first parent \"><a href=\"/index.php/en/teaching/winter-2016-17\" class=\"menu-item first parent \" ><span class=\"menu\"><span class=\"menu-title\">Winter 2016/17</span></span></a><div class=\"sp-submenu\"><div class=\"sp-submenu-wrap\"><div class=\"sp-submenu-inner clearfix\" style=\"width: 200px;\"><div class=\"megacol col1 first\" style=\"width: 200px;\"><ul class=\"sp-menu level-2\"><li class=\"menu-item first\"><a href=\"/index.php/en/teaching/winter-2016-17/is-project\" class=\"menu-item first\" ><span class=\"menu\"><span class=\"menu-title\">IS Project</span></span></a></li><li class=\"menu-item\"><a href=\"/index.php/en/teaching/winter-2016-17/dbis-seminar\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">DBIS Seminar</span></span></a></li><li class=\"menu-item last\"><a href=\"/index.php/en/teaching/winter-2016-17/datenbanksysteme\" class=\"menu-item last\" ><span class=\"menu\"><span class=\"menu-title\">Datenbanksysteme</span></span></a></li></ul></div></div></div></div></li><li class=\"menu-item parent \"><a href=\"/index.php/en/teaching/summer-2016\" class=\"menu-item parent \" ><span class=\"menu\"><span class=\"menu-title\">Summer 2016</span></span></a><div class=\"sp-submenu\"><div class=\"sp-submenu-wrap\"><div class=\"sp-submenu-inner clearfix\" style=\"width: 200px;\"><div class=\"megacol col1 first\" style=\"width: 200px;\"><ul class=\"sp-menu level-2\"><li class=\"menu-item first\"><a href=\"/index.php/en/teaching/summer-2016/informationssysteme\" class=\"menu-item first\" ><span class=\"menu\"><span class=\"menu-title\">Informationssysteme</span></span></a></li><li class=\"menu-item last\"><a href=\"http://wwwlgis.informatik.uni-kl.de/cms/courses/archive/seminar/ss16/\" target=\"_blank\" class=\"menu-item last\" ><span class=\"menu\"><span class=\"menu-title\">DBIS Seminar</span></span></a></li></ul></div></div></div></div></li><li class=\"menu-item parent \"><a href=\"/index.php/en/teaching/winter-2015-16\" class=\"menu-item parent \" ><span class=\"menu\"><span class=\"menu-title\">Winter 2015/16</span></span></a><div class=\"sp-submenu\"><div class=\"sp-submenu-wrap\"><div class=\"sp-submenu-inner clearfix\" style=\"width: 200px;\"><div class=\"megacol col1 first\" style=\"width: 200px;\"><ul class=\"sp-menu level-2\"><li class=\"menu-item first\"><a href=\"/index.php/en/teaching/winter-2015-16/datenbanksysteme\" class=\"menu-item first\" ><span class=\"menu\"><span class=\"menu-title\">Datenbanksysteme</span></span></a></li><li class=\"menu-item\"><a href=\"/index.php/en/teaching/winter-2015-16/is-project\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">IS Project</span></span></a></li><li class=\"menu-item last\"><a href=\"http://wwwlgis.informatik.uni-kl.de/cms/courses/archive/seminar/ws1516/\" class=\"menu-item last\" ><span class=\"menu\"><span class=\"menu-title\">DBIS Seminar</span></span></a></li></ul></div></div></div></div></li><li class=\"menu-item parent \"><a href=\"/index.php/en/teaching/summer-2015\" class=\"menu-item parent \" ><span class=\"menu\"><span class=\"menu-title\">Summer 2015</span></span></a><div class=\"sp-submenu\"><div class=\"sp-submenu-wrap\"><div class=\"sp-submenu-inner clearfix\" style=\"width: 200px;\"><div class=\"megacol col1 first\" style=\"width: 200px;\"><ul class=\"sp-menu level-2\"><li class=\"menu-item first\"><a href=\"/index.php/en/teaching/summer-2015/distributed-data-management\" class=\"menu-item first\" ><span class=\"menu\"><span class=\"menu-title\">Distributed Data Management</span></span></a></li><li class=\"menu-item\"><a href=\"/index.php/en/teaching/summer-2015/information-retrieval-and-data-mining\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">Information Retrieval and Data Mining (IRDM)</span></span></a></li><li class=\"menu-item last\"><a href=\"http://wwwlgis.informatik.uni-kl.de/cms/courses/archive/seminar/ss15/\" class=\"menu-item last\" ><span class=\"menu\"><span class=\"menu-title\">DBIS Seminar</span></span></a></li></ul></div></div></div></div></li><li class=\"menu-item parent \"><a href=\"/index.php/en/teaching/wintersemester-2014-15\" class=\"menu-item parent \" ><span class=\"menu\"><span class=\"menu-title\">Winter 2014/15</span></span></a><div class=\"sp-submenu\"><div class=\"sp-submenu-wrap\"><div class=\"sp-submenu-inner clearfix\" style=\"width: 200px;\"><div class=\"megacol col1 first\" style=\"width: 200px;\"><ul class=\"sp-menu level-2\"><li class=\"menu-item first\"><a href=\"/index.php/en/teaching/wintersemester-2014-15/datenbankanwendung-ws-2014-15\" class=\"menu-item first\" ><span class=\"menu\"><span class=\"menu-title\">Datenbankanwendung WS 2014/15</span></span></a></li><li class=\"menu-item\"><a href=\"/index.php/en/teaching/wintersemester-2014-15/is-project-2014-15\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">IS Project 2014/15</span></span></a></li><li class=\"menu-item last\"><a href=\"http://wwwlgis.informatik.uni-kl.de/cms/courses/archive/seminar/ws1415/\" target=\"_blank\" class=\"menu-item last\" ><span class=\"menu\"><span class=\"menu-title\">DBIS Seminar</span></span></a></li></ul></div></div></div></div></li><li class=\"menu-item last parent \"><a href=\"/index.php/en/teaching/sommersemester-2013\" class=\"menu-item last parent \" ><span class=\"menu\"><span class=\"menu-title\">Summer 2013</span></span></a><div class=\"sp-submenu\"><div class=\"sp-submenu-wrap\"><div class=\"sp-submenu-inner clearfix\" style=\"width: 200px;\"><div class=\"megacol col1 first\" style=\"width: 200px;\"><ul class=\"sp-menu level-2\"><li class=\"menu-item first\"><a href=\"/index.php/en/teaching/sommersemester-2013/distributed-data-management\" class=\"menu-item first\" ><span class=\"menu\"><span class=\"menu-title\">Distributed Data Management</span></span></a></li></ul></div></div></div></div></li></ul></div></div></div></div></li><li class=\"menu-item\"><a href=\"/index.php/en/research\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">Research</span></span></a></li><li class=\"menu-item\"><a href=\"/index.php/en/contact\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">Contact</span></span></a></li><li class=\"menu-item last\"><a href=\"/index.php/en/news\" class=\"menu-item last\" ><span class=\"menu\"><span class=\"menu-title\">News</span></span></a></li></ul>        \n" +
             "\t\t\t</div>  \t\t\t\t\n" +
             "\t\t\t</div>\n" +
             "</div></section><section id=\"sp-main-body-wrapper\" \n" +
             "                class=\" \"><div class=\"container\"><div class=\"row-fluid\" id=\"main-body\">\n" +
             "<div id=\"sp-message-area\" class=\"span12\"><section id=\"sp-component-area-wrapper\" \n" +
             "                class=\" \"><div class=\"row-fluid\" id=\"component-area\">\n" +
             "<div id=\"sp-component-area\" class=\"span12\"><section id=\"sp-component-wrapper\"><div id=\"sp-component\"><div id=\"system-message-container\">\n" +
             "\t</div>\n" +
             " \n" +
             "<article class=\"item-page post-24 post hentry status-publish category-people \">\n" +
             " \n" +
             "\t \n" +
             "\t \n" +
             "\t\t<header class=\"entry-header\">\n" +
             "\t\t\t\t\t<h1 class=\"entry-title page-header\">\n" +
             "\t\t\t\t\t\t\t\t\t<a href=\"/index.php/en/people/evica-milchevski\">\n" +
             "\t\t\t\tMSc Evica Milchevski</a>\n" +
             "\t\t\t\t\t\t</h1>\n" +
             "\t\t\t\n" +
             "\t</header>\n" +
             "\t\t\t\n" +
             "\t\n" +
             "\t\t\n" +
             "\t<section class=\"entry-content\"> \n" +
             "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<p><img class=\"pull-left\" src=\"/images/evica.jpg\" alt=\"\" width=\"211\" height=\"232\" /><b>Email</b>:  milchevski AT cs . uni-kl . de  <br /> <b>Office: </b>36/318 <br /> <b>Office Hours: </b>Wednesdays 13:30-15:30 <br /> <b>Phone</b>: +49 631 205 3274 <br /> <b>Fax</b>: +49 631 205 3299</p>\n" +
             "<address style=\"padding-left: 30px;\">University of Kaiserslautern<br /><span style=\"line-height: 1.2em;\">Department of Computer Science<br /></span>AG DBIS<br />P. O. Box 3049<br />67653 Kaiserslautern, Germany</address><address style=\"padding-left: 30px;\"> </address><address style=\"padding-left: 30px;\"> </address>\n" +
             "<h3>Reseach Interests</h3>\n" +
             "<address>\n" +
             "<ul class=\"csc-bulletlist csc-bulletlist-0\">\n" +
             "<li class=\"odd\">Algorithms and Indices for Similarity Search/Joins</li>\n" +
             "<li class=\"even\">Large-Scale Data Management and Analytics</li>\n" +
             "</ul>\n" +
             "<p> </p>\n" +
             "<h3> Publications</h3>\n" +
             "<h4>2016</h4>\n" +
             "<p><ul class=\"papermanager_front_list\"><li class=\"papermanager_front_list_item\"><p>Manuel Hoffmann, Evica Milchevski, Sebastian Michel. Playing LEGO with JSON: Probabilistic Joins over Attribute-Value Fragments. 4th International Workshop on Keyword Search and Data Exploration on Structured Data (KEYS), co-located with ICDE, Helsinki, Finland, 2016. <a href=\"/files/papers/legoJsonKEYS2016.pdf\">pdf (copyright IEEE)</a></p></li><li class=\"papermanager_front_list_item\"><p>Evica Milchevski, Sebastian Michel. Quantifying Likelihood of Change through Update Propagation across Top-k Rankings. <span class=\"st\">19th International Conference on Extending Database Technology (EDBT),  Bordeaux, France, March 2016. Poster track.<br /></span></p></li><li class=\"papermanager_front_list_item\"><p>Kiril Panev, Evica Milchevski, Sebastian Michel. Computing Similar Entity Rankings via Reverse Engineering of Top-k Database Queries. 4th International Workshop on Keyword Search and Data Exploration on Structured Data (KEYS), co-located with ICDE, Helsinki, Finland, 2016. <a href=\"/files/papers/panevSimRankings.pdf\" rel=\"alternate\">pdf (copyright IEEE)</a></p></li></ul></p>\n" +
             "<h4>2015</h4>\n" +
             "<p><ul class=\"papermanager_front_list\"><li class=\"papermanager_front_list_item\"><p>Evica Milchevski, Avishek Anand, Sebastian Michel. The Sweet Spot between Inverted Indices and Metric-Space Indexing for Top-K-List Similarity Search. <span class=\"st\">18th International Conference on Extending Database Technology</span> (<a href=\"http://edbticdt2015.be/\" rel=\"alternate\">EDBT</a>), Brussels, Belgium, 2015. [<a href=\"/files/talks/EDBT2015_Milchevski.pdf\" rel=\"alternate\">slides</a>]</p></li><li class=\"papermanager_front_list_item\"><p>Evica Milchevski, Sebastian Michel. ligDB - Online Query Processing Without (almost) any Storage. <span class=\"st\">18th International Conference on Extending Database Technology</span> (<a href=\"http://edbticdt2015.be/\" rel=\"alternate\">EDBT</a>), Brussels, Belgium, 2015. [<a href=\"/files/talks/ligDB_EDBT2015.pdf\" rel=\"alternate\">slides</a>]</p></li></ul></p>\n" +
             "<h4>2013</h4>\n" +
             "<p><ul class=\"papermanager_front_list\"><li class=\"papermanager_front_list_item\"><p>Evica Ilieva, Aleksandar Stupar, Sebastian Michel. The Essence of Knowledge (Bases) through Entity Rankings. 22nd ACM International Conference on Information and Knowledge Management (<a href=\"http://www.cikm2013.org/\">CIKM 2013</a>). Poster track.</p></li><li class=\"papermanager_front_list_item\"><p>Foteini Alvanaki, Evica Ilieva, Sebastian Michel, Aleksandar Stupar. Interesting Event Detection through Hall of Fame Rankings. Third ACM SIGMOD Workshop on Databases and Social Networks (<a href=\"https://sites.google.com/site/dbsocial13/\">DBSOCIAL 2013</a>), in conjunction with SIGMOD 2013, New York, NY, USA.</p></li></ul></p>\n" +
             "<p>  </p>\n" +
             "<h3>Education</h3>\n" +
             "<p>10/2011 – 09/2013 - Masters Degree (with honors) in Computer Science, Saarland University, Saarbruecken, Germany</p>\n" +
             "<p>09/2005 – 03/2010 - Bachelor Degree in Computer Science, University “Ss. Cyril &amp; Methodius”, Skopje, Macedonia</p>\n" +
             "<p> </p>\n" +
             "<h3>Teaching</h3>\n" +
             "<p class=\"bodytext\">WS 2014/2015 - Teaching Assistant, IS Project, TU Kaiserslautern   <br />WS 2014/2015 -Teaching Assistant, Seminar on Databases and Information Systems, TU Kaiserslautern <br />SS 2014 -Teaching Assistant, Distributed Databases Seminar, Saarland University <br />SS 2013  -Teaching Assistant, Artificial Intelligence, Saarland University <br />WS 2008/2009 -Teaching Assistant, Web Design, University “Ss. Cyril &amp; Methodius”, Skopje</p>\n" +
             "<h3>Talks</h3>\n" +
             "<p><strong>EDBT 2015 </strong></p>\n" +
             "<p>The Sweet Spot between Inverted Indices and Metric-Space Indexing for Top-K-List Similarity Search [<a href=\"/files/talks/EDBT2015_Milchevski.pdf\" rel=\"alternate\">pdf</a>][<a href=\"/files/talks/EDBT2015_Milchevski.pptx\" rel=\"alternate\">ppt</a>]</p>\n" +
             "<p>ligDB - Online Query Processing Without (almost) any Storage [<a href=\"/files/talks/ligDB_EDBT2015.pdf\" rel=\"alternate\">pdf</a>][<a href=\"/files/talks/ligDB_EDBT2015.pptx\" rel=\"alternate\">ppt</a>]</p>\n" +
             "</address> \t\t\t\t\t\t\t\t\n" +
             "\t\t\t\t\t\n" +
             "\t\t\t\t\n" +
             "\t\t\t\t\n" +
             "\t\t\t\t\t\t\t\t\n" +
             "\t\t\n" +
             "\t\t\t\t\t\t\t\n" +
             "\t\t\n" +
             "    </footer>\n" +
             "</article></div></section></div>\n" +
             "</div></section></div>\n" +
             "</div></div></section><section id=\"sp--wrapper\" \n" +
             "                class=\" \"><div class=\"row-fluid\" id=\"\">\n" +
             "<div id=\"sp-googleanalytics\" class=\"span12\">\n" +
             "\n" +
             "<div class=\"custom\"  >\n" +
             "\t<p> </p>\n" +
             "<script>// <![CDATA[\n" +
             "  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){\n" +
             "  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),\n" +
             "  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)\n" +
             "  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');\n" +
             "\n" +
             "  ga('create', 'UA-60196722-1', 'auto');\n" +
             "  ga('send', 'pageview');\n" +
             "// ]]></script></div>\n" +
             "</div>\n" +
             "</div></section>\t\n" +
             "\n" +
             "\t\t<a class=\"hidden-desktop btn btn-inverse sp-main-menu-toggler\" href=\"#\" data-toggle=\"collapse\" data-target=\".nav-collapse\">\n" +
             "\t\t\t<i class=\"icon-align-justify\"></i>\n" +
             "\t\t</a>\n" +
             "\n" +
             "\t\t<div class=\"hidden-desktop sp-mobile-menu nav-collapse collapse\">\n" +
             "\t\t\t<ul class=\"\"><li class=\"menu-item first\"><a href=\"http://dbis.informatik.uni-kl.de/\" class=\"menu-item first\" ><span class=\"menu\"><span class=\"menu-title\">Home</span></span></a></li><li class=\"menu-item active parent\"><a href=\"/index.php/en/people\" class=\"menu-item active parent\" ><span class=\"menu\"><span class=\"menu-title\">People</span></span></a><span class=\"sp-menu-toggler collapsed\" data-toggle=\"collapse\" data-target=\".collapse-111\"><i class=\"icon-angle-right\"></i><i class=\"icon-angle-down\"></i></span><ul class=\"collapse collapse-111\"><li class=\"menu-item first\"><a href=\"/index.php/en/people/michel\" class=\"menu-item first\" ><span class=\"menu\"><span class=\"menu-title\">Prof. Sebastian Michel</span></span></a></li><li class=\"menu-item\"><a href=\"/index.php/en/people/heike-neu\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">Heike Neu</span></span></a></li><li class=\"menu-item\"><a href=\"/index.php/en/people/steffen-reithermann\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">Steffen Reithermann</span></span></a></li><li class=\"menu-item\"><a href=\"/index.php/en/people/manuel-hoffmann\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">Manuel Hoffmann</span></span></a></li><li class=\"menu-item active\"><a href=\"/index.php/en/people/evica-milchevski\" class=\"menu-item active\" ><span class=\"menu\"><span class=\"menu-title\">Evica Milchevski</span></span></a></li><li class=\"menu-item\"><a href=\"/index.php/en/people/kiril-panev\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">Kiril Panev</span></span></a></li><li class=\"menu-item\"><a href=\"/index.php/en/people/koninika-pal\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">Koninika Pal</span></span></a></li><li class=\"menu-item last\"><a href=\"/index.php/en/people/alumni\" class=\"menu-item last\" ><span class=\"menu\"><span class=\"menu-title\">Alumni</span></span></a></li></ul></li><li class=\"menu-item parent\"><a href=\"/index.php/en/teaching\" class=\"menu-item parent\" ><span class=\"menu\"><span class=\"menu-title\">Teaching</span></span></a><span class=\"sp-menu-toggler collapsed\" data-toggle=\"collapse\" data-target=\".collapse-113\"><i class=\"icon-angle-right\"></i><i class=\"icon-angle-down\"></i></span><ul class=\"collapse collapse-113\"><li class=\"menu-item first parent\"><a href=\"/index.php/en/teaching/winter-2016-17\" class=\"menu-item first parent\" ><span class=\"menu\"><span class=\"menu-title\">Winter 2016/17</span></span></a><span class=\"sp-menu-toggler collapsed\" data-toggle=\"collapse\" data-target=\".collapse-169\"><i class=\"icon-angle-right\"></i><i class=\"icon-angle-down\"></i></span><ul class=\"collapse collapse-169\"><li class=\"menu-item first\"><a href=\"/index.php/en/teaching/winter-2016-17/is-project\" class=\"menu-item first\" ><span class=\"menu\"><span class=\"menu-title\">IS Project</span></span></a></li><li class=\"menu-item\"><a href=\"/index.php/en/teaching/winter-2016-17/dbis-seminar\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">DBIS Seminar</span></span></a></li><li class=\"menu-item last\"><a href=\"/index.php/en/teaching/winter-2016-17/datenbanksysteme\" class=\"menu-item last\" ><span class=\"menu\"><span class=\"menu-title\">Datenbanksysteme</span></span></a></li></ul></li><li class=\"menu-item parent\"><a href=\"/index.php/en/teaching/summer-2016\" class=\"menu-item parent\" ><span class=\"menu\"><span class=\"menu-title\">Summer 2016</span></span></a><span class=\"sp-menu-toggler collapsed\" data-toggle=\"collapse\" data-target=\".collapse-165\"><i class=\"icon-angle-right\"></i><i class=\"icon-angle-down\"></i></span><ul class=\"collapse collapse-165\"><li class=\"menu-item first\"><a href=\"/index.php/en/teaching/summer-2016/informationssysteme\" class=\"menu-item first\" ><span class=\"menu\"><span class=\"menu-title\">Informationssysteme</span></span></a></li><li class=\"menu-item last\"><a href=\"http://wwwlgis.informatik.uni-kl.de/cms/courses/archive/seminar/ss16/\" target=\"_blank\" class=\"menu-item last\" ><span class=\"menu\"><span class=\"menu-title\">DBIS Seminar</span></span></a></li></ul></li><li class=\"menu-item parent\"><a href=\"/index.php/en/teaching/winter-2015-16\" class=\"menu-item parent\" ><span class=\"menu\"><span class=\"menu-title\">Winter 2015/16</span></span></a><span class=\"sp-menu-toggler collapsed\" data-toggle=\"collapse\" data-target=\".collapse-161\"><i class=\"icon-angle-right\"></i><i class=\"icon-angle-down\"></i></span><ul class=\"collapse collapse-161\"><li class=\"menu-item first\"><a href=\"/index.php/en/teaching/winter-2015-16/datenbanksysteme\" class=\"menu-item first\" ><span class=\"menu\"><span class=\"menu-title\">Datenbanksysteme</span></span></a></li><li class=\"menu-item\"><a href=\"/index.php/en/teaching/winter-2015-16/is-project\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">IS Project</span></span></a></li><li class=\"menu-item last\"><a href=\"http://wwwlgis.informatik.uni-kl.de/cms/courses/archive/seminar/ws1516/\" class=\"menu-item last\" ><span class=\"menu\"><span class=\"menu-title\">DBIS Seminar</span></span></a></li></ul></li><li class=\"menu-item parent\"><a href=\"/index.php/en/teaching/summer-2015\" class=\"menu-item parent\" ><span class=\"menu\"><span class=\"menu-title\">Summer 2015</span></span></a><span class=\"sp-menu-toggler collapsed\" data-toggle=\"collapse\" data-target=\".collapse-140\"><i class=\"icon-angle-right\"></i><i class=\"icon-angle-down\"></i></span><ul class=\"collapse collapse-140\"><li class=\"menu-item first\"><a href=\"/index.php/en/teaching/summer-2015/distributed-data-management\" class=\"menu-item first\" ><span class=\"menu\"><span class=\"menu-title\">Distributed Data Management</span></span></a></li><li class=\"menu-item\"><a href=\"/index.php/en/teaching/summer-2015/information-retrieval-and-data-mining\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">Information Retrieval and Data Mining (IRDM)</span></span></a></li><li class=\"menu-item last\"><a href=\"http://wwwlgis.informatik.uni-kl.de/cms/courses/archive/seminar/ss15/\" class=\"menu-item last\" ><span class=\"menu\"><span class=\"menu-title\">DBIS Seminar</span></span></a></li></ul></li><li class=\"menu-item parent\"><a href=\"/index.php/en/teaching/wintersemester-2014-15\" class=\"menu-item parent\" ><span class=\"menu\"><span class=\"menu-title\">Winter 2014/15</span></span></a><span class=\"sp-menu-toggler collapsed\" data-toggle=\"collapse\" data-target=\".collapse-115\"><i class=\"icon-angle-right\"></i><i class=\"icon-angle-down\"></i></span><ul class=\"collapse collapse-115\"><li class=\"menu-item first\"><a href=\"/index.php/en/teaching/wintersemester-2014-15/datenbankanwendung-ws-2014-15\" class=\"menu-item first\" ><span class=\"menu\"><span class=\"menu-title\">Datenbankanwendung WS 2014/15</span></span></a></li><li class=\"menu-item\"><a href=\"/index.php/en/teaching/wintersemester-2014-15/is-project-2014-15\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">IS Project 2014/15</span></span></a></li><li class=\"menu-item last\"><a href=\"http://wwwlgis.informatik.uni-kl.de/cms/courses/archive/seminar/ws1415/\" target=\"_blank\" class=\"menu-item last\" ><span class=\"menu\"><span class=\"menu-title\">DBIS Seminar</span></span></a></li></ul></li><li class=\"menu-item last parent\"><a href=\"/index.php/en/teaching/sommersemester-2013\" class=\"menu-item last parent\" ><span class=\"menu\"><span class=\"menu-title\">Summer 2013</span></span></a><span class=\"sp-menu-toggler collapsed\" data-toggle=\"collapse\" data-target=\".collapse-114\"><i class=\"icon-angle-right\"></i><i class=\"icon-angle-down\"></i></span><ul class=\"collapse collapse-114\"><li class=\"menu-item first\"><a href=\"/index.php/en/teaching/sommersemester-2013/distributed-data-management\" class=\"menu-item first\" ><span class=\"menu\"><span class=\"menu-title\">Distributed Data Management</span></span></a></li></ul></li></ul></li><li class=\"menu-item\"><a href=\"/index.php/en/research\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">Research</span></span></a></li><li class=\"menu-item\"><a href=\"/index.php/en/contact\" class=\"menu-item\" ><span class=\"menu\"><span class=\"menu-title\">Contact</span></span></a></li><li class=\"menu-item last\"><a href=\"/index.php/en/news\" class=\"menu-item last\" ><span class=\"menu\"><span class=\"menu-title\">News</span></span></a></li></ul>   \n" +
             "\t\t</div>\n" +
             "\t\t        \n" +
             "\t\t</div>\n" +
             "        <div align=\"center\"><p>(c) AG DBIS, TU Kaiserslautern, 2015\n" +
             "          </p>\n" +
             "          </p></div>\n" +
             "    </body>\n" +
             "</html>\n";
    }

}

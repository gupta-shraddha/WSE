package Miscellaneous;

import java.util.*;

/**
 * Created on 12/20/2016.
 */
public class Snippets {
    private static final int maxSnippetSize = 32;   //32terms
    private static final int minSnippetSize = 8;    //8terms
    private static List<String> qTerms; // query keywords
    private static String[] textContent; // content from the page in array format

    public int getStartIndex() {
        return startIndex;
    }
    private int startIndex = 0;

    private int endIndex = 0;

    private double snippetScore=0f;
    private double getSnippetScore(){
        return snippetScore;
    }

    private int snippetWordCount = 0;
    private int snippetRelevantWordCount = 0; //All qterms in the snippet
    private int snippetRelevantDistinctWordCount = 0; //single occurrence of qterms in snippet

    public Snippets() // for creating object of class without constructor from HTML page..
    {

    }
    public Snippets(int iStart,int iEnd) // constructor to make multiple snippets based on their start and end position in textcontent
    {
        startIndex=iStart;
        endIndex=iEnd;
        String []snippetArray= Arrays.copyOfRange(textContent,startIndex,endIndex+1);
        snippetWordCount=snippetArray.length;
        Set<String> qTermsSnippet = new HashSet<>(); // query terms in snippet
        for (String term :snippetArray)
        { //Count relevant terms
            for (String s : qTerms) {
                if(term.equalsIgnoreCase(s)) {
                    snippetRelevantWordCount++;
                    qTermsSnippet.add(s);
                }
            }
        }
        snippetRelevantDistinctWordCount = qTermsSnippet.size();
        snippetScore=((double)snippetRelevantWordCount/snippetWordCount)*((double)snippetRelevantDistinctWordCount/qTerms.size())*(1-((double)(snippetWordCount)/maxSnippetSize));
                    //ratio --> relevant over total * distinct over qterms * probability of snippet size w.r.t to maxsize
    }

    public static String createSnippet(String text, List<String> terms) {
        // list.indexOf(object) will return same indices again.... use String[]....
        List<Integer> positions = new ArrayList<>();
        textContent =text.split("\\s+|\\W+"); // splitting the text content of page into string array.
        qTerms =terms;
        for (int i=0; i<textContent.length;i++) {
            for (String term : qTerms)
            {
                if (textContent[i].equalsIgnoreCase(term)) {
                    // if search for wednesdays ... wednesday is also returned and vice versa.... (but not bold)
                    positions.add(i); // for each qterm in textcontent mark its position..
                }
            }
        }
        Set<Snippets> snippetsSet=new HashSet<>(multiSnippets(positions)); // create min size snippets based on qterms position in textcontent
        // join snippets based on their starting position if they are near and concatenating doesn't exceed maxsnippetsize... good to go
        snippetsSet= joinSnippets(snippetsSet);

        List<Snippets> snippetList = new ArrayList<>(snippetsSet); //convert the Set to list as SET cannot be sorted
        Collections.sort(snippetList, Comparator.comparingDouble(Snippets::getSnippetScore)); // sort the snippets based on Score
        Collections.reverse(snippetList); // sorting in reverse order.. highest score on 1st .....
        String finalSnippet="";
        if(snippetList.size()==0) // no snippet found
        {
            return "";
        }
        if(snippetList.size()==1) { // only one snippet found // turn to string and return
            Snippets selectedSnippet = snippetList.get(0); //
            finalSnippet+=snippet2String(selectedSnippet);
            return finalSnippet;
        }
        if(snippetList.size()>1)
        {
            int count = 0;
            int i = 0; // first index has the highest score
            while(i<snippetList.size() && count + minSnippetSize < maxSnippetSize )
            {
                Snippets tempSnpt = snippetList.get(i);
                //Set<String> tempSnptSet =new HashSet<>(Arrays.asList(Arrays.copyOfRange(textContent,tempSnpt.startIndex,tempSnpt.endIndex+1)));
                //wrong idea as set intersection will always be zero
                i++; // descending order of snippetScore
                if(tempSnpt.snippetWordCount + count <= maxSnippetSize )
                {
                    finalSnippet+=snippet2String(tempSnpt);
                    finalSnippet+="...";
                    count += tempSnpt.snippetWordCount;
                }
            }
        }
        List<String> missinqTerms= new ArrayList<>(); // Missingterms
        for(String term:qTerms)
        {
            if(!Arrays.asList(textContent).contains(term))
            {
                missinqTerms.add(term);
            }
            else
            {
                continue;
            }
        }
        return missinqTerms.size()>=1?finalSnippet+"\n Missing term <b>"+missinqTerms.toString()+"</b>":finalSnippet;
    }

    private static String snippet2String(Snippets tempSnippet) {
        String finalSnippet="";
        for (String s:Arrays.copyOfRange(textContent,tempSnippet.startIndex,tempSnippet.endIndex+1))
        {
            finalSnippet += qTerms.contains(s)?s.replaceAll(s.toLowerCase(),"<b>"+s+" </b>"):s+" ";
            //bold the qterms within the snippet
        }
        return finalSnippet;
    }

    private static List<Snippets> multiSnippets(List<Integer> positions)
    {
        List<Snippets> snippetList= new ArrayList<>();
        for (Integer pos : positions)
        {
            int startPos = Math.max(0,pos-4); //previous 4
            int endPos = Math.min(textContent.length-1,pos+3); //following 4
            int snipSize=endPos-startPos+1;
            if(snipSize >=minSnippetSize && snipSize <= maxSnippetSize) //>=8 && <=32 // initially restricting to only 8
            {
                snippetList.add(new Snippets(startPos, endPos));
            }
            else if(snipSize < minSnippetSize || snipSize > maxSnippetSize) //making snippets of 8 ..>maxSnippetSize not required now
            {
                endPos = startPos==0? minSnippetSize-1:endPos;
                startPos= endPos == textContent.length-1?endPos-minSnippetSize+1:startPos;
                snippetList.add(new Snippets(startPos, endPos));
            }
        }
        return snippetList;
    }
    private static Set<Snippets> joinSnippets(Set<Snippets> snippetsSet)
    {
        boolean flag=true;
        while(flag)
        {
            List<Snippets> snippetList = new ArrayList<>(snippetsSet);
            Collections.sort(snippetList, Comparator.comparingInt(Snippets::getStartIndex)); // sort based on starting index
            flag = false;
            for (int i = 1; i < snippetList.size(); i++)
            {
                int startPos = Math.min(snippetList.get(i-1).startIndex,snippetList.get(i).startIndex);
                int endPos = Math.max(snippetList.get(i-1).endIndex,snippetList.get(i).endIndex);
                int snippetSize=endPos-startPos+1;
                Snippets combineSnippet=(snippetSize <= maxSnippetSize)?new Snippets(startPos, endPos):null;
                if(combineSnippet != null)
                {
                    if(snippetsSet.add(combineSnippet)) //always returning true. :/
                    {
                        flag = true;
                    }
                }
            }
        }
        return snippetsSet;
    }
    /*
    HashSet internally uses the hashCode and equals methods to tell if two objects are equal.
    For arrays,list hashCode and equals don't look at the contents of the array/list. Instead, they just produce a hash code based on
    the identity of the object, and two arrays/list compare equal if and only if they're the same object.
    This means that if you put an array,list into a HashSet and then try looking up that array after changing the contents,
    it will always find it.
     */
    public static void main (String[] args)  {
        String text = "In Web development, snippets often contain HTML code. An HTML snippet might be used to insert a formatted table, a Web form, or a block of text. CSS snippets may be used to apply formatting to a Web page. Web scripting snippets written in JavaScript, PHP, or another scripting language may be used to streamline dynamic Web page development. For example, a PHP snippet that contains database connection information can be inserted into each page that accesses information from a database. Whether programming software or developing websites, using snippets can save the developers a lot of time";
        ArrayList<String> terms  = new ArrayList<>();
        terms.add("lot");
        terms.add("time");
        terms.add("snippets");
        terms.add("developers");
        terms.add("music");
        terms.add("using");
        terms.add("German");
        String result = createSnippet(text,terms);
        System.out.println(result);
    }
    @Override
    public boolean equals(Object obj1)
    {
        if(this == obj1)
            return true;
        if(!(obj1 instanceof Snippets))
            return false;
        Snippets obj2 = (Snippets) obj1;
        if (this.startIndex == obj2.startIndex && this.endIndex == obj2.endIndex)
            return true;

        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.startIndex,this.endIndex);
    }

}

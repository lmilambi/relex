import java.awt.List;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Random;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

// public class morphemeCode implements MorphSplitInterface {
public class morphemeCode {
	
	public int listen_port;
	public int max_to_parse;
	/**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
    	morphemeCode m = new morphemeCode();
		m.listen_port = 4445;
      m.max_to_parse = 3;
    	m.run();      
		
    }
	
	public morphemeCode()
	{
		
	}
	
	@SuppressWarnings("resource")
	public void run()
	{
    	
		MorphemeSplitter splitter = new MorphemeSplitter();
    	// -----------------------------------------------------------------
    	// Socket setup
    	// morphemeCode s = new morphemeCode();
    	// s.listen_port = listen_port;
    	ServerSocket listen_sock = null;

    	try
    	{
    		listen_sock = new ServerSocket(listen_port);
    	}
    	catch (IOException e)
    	{
    		System.err.println("Error: Listen failed on port " + listen_port);
    		System.exit(-1);
    	}
    		System.err.println("Info: Listening on port " + listen_port);
    
    // -----------------------------------------------------------------
   	// Main loop -- listen for connections, accept them, and process.
    while (true)
    {
    		Socket in_sock = null;
    		InputStream ins = null;
        	OutputStream outs = null;
        	PrintWriter out = null;

    		try {
    			System.err.println("Info: Waiting for socket connection");
    			in_sock = listen_sock.accept();
    			in_sock.setKeepAlive(true);
				System.err.println("Socket connected");
    			ins = in_sock.getInputStream();
				outs = in_sock.getOutputStream();
				out = new PrintWriter(outs, true);

    		} catch (IOException e) {
    				System.err.println("Error: Accept failed: " + e.getMessage());
    			continue;
    		}

    		System.err.println("Info: Socket accept");
         BufferedReader in = new BufferedReader(new InputStreamReader(ins));
    		
    		// ---------------------------------------------------------
    		//Loop through line in input, delete punctuation, and separate each word
    	    //for splitting into morphemes
         while (true) {

               String sentence = null;

               try {
                  // Break if EOF encountered.  This should have been easy
                  // to figure out, but its not. Java sux rox. What is wrong
                  // with these people? Are they all stupid, or what?
                  // Arghhhh.
                  int one_char = in.read();
                  // 0x4 is ASCII EOT aka ctrl-D via telnet.
                  if (-1 == one_char || 4 == one_char)
                  {
                     break;
                  }
                  if ('\r' == one_char)
                     // continue;
                     break;
                  if ('\n' == one_char)
                     // continue;
                     break;

                  // Another bright shining example of more java idiocy.
                  char junk[] = {(char)one_char};
                  String line = new String(junk);
                  line += in.readLine();

                  sentence = line;

                  System.err.println("Info: recv input: \"" + line + "\"");

                  int slen = sentence.length();
                  if (0<slen && '\n' == sentence.charAt(slen-1)) break;
                  if (0<slen && '\r' == sentence.charAt(slen-1)) break;
               }
               catch (Exception e)
               {
                  System.err.println("Error: Read of input failed:" + e.getMessage());
                  break;
               }

               // If the sentence is null; we've run out of input.
               if (null == sentence || sentence.equals(""))
                  break;

    		
			System.err.println("Starting morpheme splitter");
			String output = splitter.Start(sentence, max_to_parse);
			out.println(output);
			System.err.println("Finished morpheme splitter");

                  break;
         }

    		try
			{
				in_sock.close();
				System.err.println("Info: Closed input socket");
			}
			catch (IOException e)
			{
				System.err.println("Error: Socket close failed: " + e.getMessage());
			}	
    	}
	}
}

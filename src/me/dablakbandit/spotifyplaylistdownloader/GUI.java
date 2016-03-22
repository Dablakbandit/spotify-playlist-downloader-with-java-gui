package me.dablakbandit.spotifyplaylistdownloader;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

@SuppressWarnings("serial")
public class GUI extends JFrame{

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args){
		try{
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		}catch(Throwable e){
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				try{
					GUI frame = new GUI();
					frame.setVisible(true);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}

	protected JTextPane username, password, destination, url;
	protected JProgressBar progress;
	protected JLabel done;
	protected JTextArea output;
	protected JButton start;

	public GUI(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Spotify Playlist Downloader");
		setBounds(100, 100, 435, 442);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(10, 17, 81, 14);
		contentPane.add(lblUsername);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(10, 42, 81, 14);
		contentPane.add(lblPassword);

		username = new JTextPane();
		username.setBounds(91, 12, 226, 20);
		contentPane.add(username);

		password = new JTextPane();
		password.setBounds(91, 37, 226, 20);
		contentPane.add(password);

		JButton btnSave = new JButton("Save Login");
		btnSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				save();
			}
		});
		btnSave.setBounds(212, 68, 105, 23);
		contentPane.add(btnSave);

		JLabel lblTextStoredIn = new JLabel("Credentials stored in plain text", SwingConstants.RIGHT);
		lblTextStoredIn.setBounds(91, 98, 226, 14);
		contentPane.add(lblTextStoredIn);

		JLabel label = new JLabel("Progress:");
		label.setBounds(10, 117, 81, 14);
		contentPane.add(label);

		progress = new JProgressBar();
		progress.setBounds(91, 118, 226, 14);
		contentPane.add(progress);

		done = new JLabel("0/0", SwingConstants.CENTER);
		done.setBounds(91, 135, 226, 16);
		contentPane.add(done);

		JLabel lblOutput = new JLabel("Destination:");
		lblOutput.setBounds(10, 160, 81, 16);
		contentPane.add(lblOutput);

		JButton button = new JButton("Open");
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				open();
			}
		});
		button.setBounds(329, 157, 73, 23);
		contentPane.add(button);

		destination = new JTextPane();
		destination.setBounds(91, 159, 226, 20);
		contentPane.add(destination);

		JLabel lblUrl = new JLabel("URL:");
		lblUrl.setBounds(10, 188, 81, 16);
		contentPane.add(lblUrl);

		url = new JTextPane();
		url.setBounds(91, 186, 226, 20);
		contentPane.add(url);

		JLabel lblExample = new JLabel("Example:");
		lblExample.setBounds(10, 210, 81, 16);
		contentPane.add(lblExample);

		JLabel example = new JLabel("https://play.spotify.com/user/spotify/playlist/6RsopNg2yrLjKiu00jaCyi");
		example.setBounds(10, 228, 392, 16);
		contentPane.add(example);

		JLabel lblGuiMadeBy = new JLabel("GUI made by Dablakbandit", SwingConstants.RIGHT);
		lblGuiMadeBy.setBounds(96, 375, 311, 16);
		contentPane.add(lblGuiMadeBy);

		start = new JButton("Start");
		start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				start();
			}
		});
		start.setBounds(329, 183, 73, 23);
		contentPane.add(start);

		JLabel lblOutput_1 = new JLabel("Output", SwingConstants.CENTER);
		lblOutput_1.setBounds(10, 256, 392, 16);
		contentPane.add(lblOutput_1);

		output = new JTextArea();
		JScrollPane scroll = new JScrollPane(output);
		DefaultCaret caret = (DefaultCaret)output.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		output.setEditable(false);
		scroll.setBounds(10, 274, 392, 89);
		contentPane.add(scroll);

		JButton button_1 = new JButton("NPM Install");
		button_1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				installNode();
			}
		});
		button_1.setBounds(91, 68, 105, 23);
		contentPane.add(button_1);
		this.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent windowEvent) {
		    	if(current!=null){
		    		current.destroy();
		    	}
		    }
		});
	}

	protected NodeThread nt;

	protected void installNode(){
		nt = new NodeThread();
		new Thread(nt).start();
	}

	protected DownloadThread wt;

	protected void save(){
		try{
			BufferedReader in = new BufferedReader(new FileReader("main.js"));
			List<String> list = new ArrayList<String>();
			String line;
			while((line = in.readLine()) != null){
				list.add(line);
			}
			in.close();
			String[] array = list.toArray(new String[list.size()]);
			array[23] = "  USERNAME = \"" + username.getText() + "\"; //Program.username;";
			array[25] = "  PASSWORD = \"" + password.getText() + "\"; //Program.password;";
			PrintWriter pr = new PrintWriter("main.js");
			for(String s : array){
				pr.println(s);
			}
			pr.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	protected void open(){
		try{
			File f = new File(destination.getText());
			Desktop.getDesktop().open(f);
		}catch(Exception e){}
	}

	protected void start(){
		if(nt != null&&nt.getRunning()){
			nodeProcess("Installing Node");
			return;
		}
		Pattern pattern = Pattern.compile("https://(play|open).spotify.com/user/([A-Za-z0-9_\\-]+)/playlist/([A-Za-z0-9_\\-]+)");
		Matcher matcher = pattern.matcher(url.getText());
		if(matcher.matches()){
			user = matcher.group(2);
			playlist = matcher.group(3);
			boolean b = !this.destination.getText().equals("");
			out = null;
			if(b){
				out = new File(this.destination.getText());
				if(!out.exists())
					out.mkdirs();
			}
			wt = new DownloadThread();
			new Thread(wt).start();
		}else{
			output.setText("Please input url.");
		}
	}

	protected String user, playlist;
	protected File out;

	protected Process current;
	
	protected void download(){
		try{
			Runtime rt = Runtime.getRuntime();
			String command = "node main.js -u " + user + " -p " + playlist + (out != null ? " -d " + out.getPath() : "");
			downloadProcess("Command: " + command);
			current = rt.exec(command, null, new File(System.getProperty("user.dir")));
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(current.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(current.getErrorStream()));
			String s = null;
			while((s = stdInput.readLine()) != null){
				downloadProcess(s);
			}
			while((s = stdError.readLine()) != null){
				downloadProcess(s);
			}
			current = null;
			progress.setValue(100);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
    protected boolean isWindows() {
    	String OS = System.getProperty("os.name").toLowerCase();
        return (OS.indexOf("win") >= 0);
    }

    protected int tracks = 0;
    protected int sofar = 0;
    
	protected void downloadProcess(String s){
		if(s.startsWith(" - ")){
			String a = s.substring(3);
			if(a.startsWith("Processing")){
				Pattern pattern = Pattern.compile("Processing ([A-Za-z0-9_\\-]+) Tracks");
				Matcher matcher = pattern.matcher(a);
				if(matcher.matches()){
					tracks = Integer.parseInt(matcher.group(1));
					updateDone();
				}
			}else if(a.startsWith(" - ")){
				String b = a.substring(3);
				if(b.startsWith("DONE: ")){
					sofar++;
					updateDone();
				}
			}
		}else if(s.startsWith("Already Downloaded: ")){
			sofar++;
			updateDone();
		}
		System.out.println(s);
		output.setText(output.getText() + "\r\n" + s);
	}
	
	protected void updateDone(){
		done.setText(sofar + "/" + tracks);
		if(sofar!=0&&tracks!=0){
			double d = (double)sofar/(double)tracks;
			d*=100;
			progress.setValue((int)d);
		}
	}
	
	protected void nodeProcess(String s){
		System.out.println(s);
		output.setText(output.getText() + "\r\n" + s);
	}

	public class DownloadThread implements Runnable{

		public boolean running = false;

		@Override
		public void run(){
			output.setText("");
			running = true;
			progress.setValue(0);
			start.setEnabled(false);
			download();
			start.setEnabled(true);
			running = false;
		}

		public boolean getRunning(){
			return running;
		}
	}

	public class NodeThread implements Runnable{

		public boolean running = false;

		@Override
		public void run(){
			try{
				Runtime rt = Runtime.getRuntime();
				String command = "npm" + (isWindows() ? ".cmd" : "") + " install";
				downloadProcess("Command: " + command);
				current = rt.exec(command, null, new File(System.getProperty("user.dir")));
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(current.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(current.getErrorStream()));
				String s = null;
				while((s = stdInput.readLine()) != null){
					nodeProcess(s);
				}
				while((s = stdError.readLine()) != null){
					nodeProcess(s);
				}
				current = null;
				nodeProcess("Node Install Finished");
				nt = null;
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		public boolean getRunning(){
			return running;
		}
	}
}

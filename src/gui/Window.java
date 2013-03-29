package gui;

import java.awt.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component;
import javax.swing.GroupLayout.Alignment;
import net.sourceforge.vietocr.*;
import javax.imageio.ImageIO;
import javax.imageio.IIOImage;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;
import java.util.*;
import javax.swing.filechooser.FileFilter;

public class Window {

	private JFrame mainFrame = new JFrame();
	private int noOfLanguages;
	private String[] availableLanguages;
	private String[] availableLanguagesCodes;
	public String filename;

	public static final String APP_NAME = "Java OCR";
	private static final String strCurrentDirectory = "currentDirectory";
	private static final String strOutputDirectory = "outputDirectory";
	final JImageLabel imageLabel = new JImageLabel();
	final JTextArea textArea = new JTextArea();
	final Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
	final JFileChooser fileOpener = new JFileChooser();
	final JFileChooser fileSaver = new JFileChooser();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private int snap(final int ideal, final int min, final int max) {
		final int TOLERANCE = 0;
		return ideal < min + TOLERANCE ? min : (ideal > max - TOLERANCE ? max : ideal);
	}

	void quit() {
		System.exit(0);
	}
	private void initialize() {

		imageLabel.setIcon(null);
		noOfLanguages=1;//to be made dynamic
		availableLanguages = new String[noOfLanguages];
		availableLanguagesCodes = new String[noOfLanguages];
		availableLanguages[0]="English";
		availableLanguagesCodes[0]="eng";
		
		mainFrame.setTitle(APP_NAME);
		mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(Window.class.getResource("/gui/icons/ocr.png")));
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainFrame.setSize(screen.width, screen.height);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainFrame.getContentPane().setLayout(new java.awt.BorderLayout());

		JMenuBar menuBar = new JMenuBar();
		menuBar.setFocusTraversalKeysEnabled(true);
		mainFrame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);

		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);
		
		JMenu mnPageSegmentationMode = new JMenu("Page Segmentation Mode");
		mnSettings.add(mnPageSegmentationMode);
		
		JMenuItem menuItem = new JMenuItem("0");
		mnPageSegmentationMode.add(menuItem);
		
		JMenuItem menuItem_1 = new JMenuItem("1");
		mnPageSegmentationMode.add(menuItem_1);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmJavaOcrHelp = new JMenuItem(APP_NAME + " Help");
		mnHelp.add(mntmJavaOcrHelp);

		JMenuItem mntmAboutJavaOcr = new JMenuItem("About " + APP_NAME);
		mnHelp.add(mntmAboutJavaOcr);

		JToolBar toolBar = new JToolBar();
		toolBar.setAlignmentX(Component.LEFT_ALIGNMENT);

		JButton jButtonOpen = new JButton("");
		toolBar.add(jButtonOpen);
		jButtonOpen.setIcon(new ImageIcon(Window.class.getResource("/gui/icons/open.png")));
		jButtonOpen.setToolTipText("Open Image");

		JButton jButtonSave = new JButton("");
		toolBar.add(jButtonSave);
		jButtonSave.setIcon(new ImageIcon(Window.class.getResource("/gui/icons/save.png")));
		jButtonSave.setToolTipText("Save Text");

		JButton jButtonOcr = new JButton("");
		toolBar.add(jButtonOcr);
		jButtonOcr.setIcon(new ImageIcon(Window.class.getResource("/gui/icons/ocr.png")));
		jButtonOcr.setToolTipText("Perform OCR");

		toolBar.add(Box.createHorizontalGlue());

		JLabel lblSelectLangauge = new JLabel("Select Langauge");
		lblSelectLangauge.setAlignmentX(Component.RIGHT_ALIGNMENT);
		toolBar.add(lblSelectLangauge);

		DefaultComboBoxModel model = new DefaultComboBoxModel(availableLanguages);
		JComboBox comboBox = new JComboBox();
		comboBox.setMaximumSize(new Dimension(200, 24));
		comboBox.setAlignmentX(Component.RIGHT_ALIGNMENT);
		comboBox.setModel(model);
		toolBar.add(comboBox);
		mainFrame.getContentPane().add(toolBar, BorderLayout.NORTH);

		JSplitPane splitPane = new JSplitPane();
		mainFrame.getContentPane().add(splitPane, BorderLayout.CENTER);


		splitPane.setRightComponent(textArea);

		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		splitPane.setDividerLocation(screen.width/2);
		scrollPane.setViewportView(imageLabel);

		fileOpener.setDialogTitle(jButtonOpen.getToolTipText()); // NOI18N
		FileFilter allImageFilter = new SimpleFilter("bmp;gif;jpg;jpeg;jp2;png;pnm;pbm;pgm;ppm;tif;tiff", "All_Image_Files");
		FileFilter bmpFilter = new SimpleFilter("bmp", "Bitmap");
		FileFilter gifFilter = new SimpleFilter("gif", "GIF");
		FileFilter jpegFilter = new SimpleFilter("jpg;jpeg", "JPEG");
		FileFilter jpeg2000Filter = new SimpleFilter("jp2", "JPEG 2000");
		FileFilter pngFilter = new SimpleFilter("png", "PNG");
		FileFilter pnmFilter = new SimpleFilter("pnm;pbm;pgm;ppm", "PNM");
		FileFilter tiffFilter = new SimpleFilter("tif;tiff", "TIFF");

		FileFilter pdfFilter = new SimpleFilter("pdf", "PDF");
		FileFilter textFilter = new SimpleFilter("txt", "UTF-8_Text");

		fileOpener.setAcceptAllFileFilterUsed(false);
		fileOpener.addChoosableFileFilter(allImageFilter);
		fileOpener.addChoosableFileFilter(bmpFilter);
		fileOpener.addChoosableFileFilter(gifFilter);
		fileOpener.addChoosableFileFilter(jpegFilter);
		fileOpener.addChoosableFileFilter(jpeg2000Filter);
		fileOpener.addChoosableFileFilter(pngFilter);
		fileOpener.addChoosableFileFilter(pnmFilter);
		fileOpener.addChoosableFileFilter(tiffFilter);
		//fileOpener.addChoosableFileFilter(pdfFilter);

		fileSaver.setAcceptAllFileFilterUsed(false);
		fileSaver.addChoosableFileFilter(textFilter);	



		mntmOpen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				fileOpen(e);
			}

		});

		mntmSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				fileSave(e);
			}
		});

		mntmExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				quit();
			}
		});

		jButtonOpen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				fileOpen(e);
			}
		});

		jButtonSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				fileSave(e);
			}
		});

		jButtonOcr.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});

	}

	public void fileOpen(MouseEvent e) {

		int rVal = fileOpener.showOpenDialog(mainFrame);
		File file = fileOpener.getSelectedFile();
		BufferedImage image = null;
		if(rVal == JFileChooser.APPROVE_OPTION) {
			try
			{
				image = ImageIO.read(file);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				System.exit(1);
			}
			ImageIconScalable imageIcon = new ImageIconScalable(image);
			int newImageHeight;
			int newImageWidth;
			if(imageIcon.getIconHeight()>imageLabel.getHeight())
				newImageHeight=imageLabel.getHeight();
			else
				newImageHeight=imageIcon.getIconHeight();
			newImageWidth=(imageIcon.getIconWidth()*newImageHeight)/imageIcon.getIconHeight();
			if(newImageWidth>imageLabel.getWidth())
			{	
				newImageWidth=imageLabel.getWidth();
				newImageHeight=(imageIcon.getIconHeight()*newImageWidth)/imageIcon.getIconWidth();
			}
			imageIcon.setScaledSize(newImageWidth, newImageHeight);
			imageLabel.setIcon(imageIcon);
		}
	}

	public void fileSave(MouseEvent e) {
		int rVal = fileSaver.showSaveDialog(mainFrame);
		if(rVal == JFileChooser.APPROVE_OPTION) {
			File file = fileSaver.getSelectedFile();
			BufferedWriter out;
			try {
				if(file.getName().endsWith(".txt"))
					out = new BufferedWriter(new FileWriter(file));
				else
					out = new BufferedWriter(new FileWriter(file + ".txt"));
				out.write(textArea.getText());
				out.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(1);
			}
		}
	}

}

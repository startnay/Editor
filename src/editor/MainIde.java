/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.PrintJob;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.colorchooser.DefaultColorSelectionModel;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import java.awt.Robot;
import java.awt.event.KeyListener;
import javafx.scene.input.KeyCode;

/**
 *
 * @author Administrator
 */
@SuppressWarnings("serial")
public class MainIde extends JFrame
        implements TreeModelListener, WindowFocusListener,
        ActionListener, TreeSelectionListener, KeyListener {

    private byte last_state;
    private JMenuBar jMenu;
    private JPopupMenu mnuPopupMenu;

    private JMenu jMenuFile;
    private JMenu jMenuEdit;
    private JMenu jMenuRun;
    private JMenu jMenuSource;

    private JTextPane jTextArea_Code;
    private DefaultMutableTreeNode root;
    private JTree jTreeProject;
    private JSplitPane jSplitPane;
    private TreeProject treeProject;
    private JScrollPane treeView;
    private JScrollPane jEditor;
    private String selectedFile = "";
    private String lastSelectedFile = "";
    private String lastText;
    private String text;
    private JMenu jAboutMenu;
    fontSelector fontS = new fontSelector();

    UndoManager undo = new UndoManager();
    UndoAction undoAction = new UndoAction();
    RedoAction redoAction = new RedoAction();
    private JPopupMenu mnuTree = new JPopupMenu();
    private SupportedKeywords keyword = new SupportedKeywords();
    String key;
    String tab = "";

    private void initComponents() {
        key = keyword.getJavaKeyword();
        jMenu = new JMenuBar();

        jMenuFile = new JMenu("File");
        jMenuEdit = new JMenu("Edit");
        jMenuSource = new JMenu("Source");
        jMenuRun = new JMenu("Run");
        jAboutMenu = new JMenu("About");
        mnuPopupMenu = new JPopupMenu();

        JMenuItem item;

        jMenuRun.add(item = new JMenuItem("Run Project"));
        item.addActionListener(this);

        jMenuFile.add(item = new JMenuItem("New Project"));
        item.setIcon(new ImageIcon("icons/new.png"));
        item.addActionListener(this);
        jMenuFile.add(item = new JMenuItem("Open Project"));
        item.setIcon(new ImageIcon("icons/open.png"));
        item.addActionListener(this);
        jMenuFile.add(item = new JMenuItem(/*new DefaultEditorKit.DefaultKeyTypedAction()*/));
        item.setText("Save");
        item.setIcon(new ImageIcon("icons/save.png"));
        item.addActionListener(this);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        //	item.setActionCommand("Save");

        jMenuFile.add(item = new JMenuItem("Print"));
        item.addActionListener(this);
        jMenuFile.add(item = new JMenuItem("Exit"));
        item.setIcon(new ImageIcon("icons/close.png"));
        item.addActionListener(this);

        jMenuEdit.add(undoAction);
        jMenuEdit.add(redoAction);

        jMenuEdit.add(item = new JMenuItem(/*new DefaultEditorKit.DefaultKeyTypedAction()*/));
        item.setText("Find");
        item.setIcon(new ImageIcon("icons/search.png"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(this);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
        item.setActionCommand("Find");

        jMenuEdit.add(item = new JMenuItem(new DefaultEditorKit.CopyAction()));
        item.setText("Copy");
        item.setIcon(new ImageIcon("icons/copy.png"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(this);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        item.setActionCommand("Copy");

        jMenuEdit.add(item = new JMenuItem(new DefaultEditorKit.CutAction()));
        item.setText("Cut");
        item.setIcon(new ImageIcon("icons/cut.png"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        item.setActionCommand("Cut");
        item.addActionListener(this);

        jMenuEdit.add(item = new JMenuItem(new DefaultEditorKit.CutAction()));
        item.setText("Past");
        item.setIcon(new ImageIcon("icons/paste.png"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(this);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        item.setActionCommand("Past");

        item = new JMenuItem("About Us");
        item.setActionCommand("About Us");
        item.addActionListener(this);
        jAboutMenu.add(item);

        item = new JMenuItem("About Softwares");
        item.setActionCommand("About Software");
        item.addActionListener(this);
        jAboutMenu.add(item);

        mnuPopupMenu.add(item = new JMenuItem(new DefaultEditorKit.CopyAction()));
        item.setText("Copy");
        item.setIcon(new ImageIcon("icons/copy.png"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(this);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        item.setActionCommand("Copy");

        mnuPopupMenu.add(item = new JMenuItem(new DefaultEditorKit.CutAction()));
        item.setText("Cut");
        item.setIcon(new ImageIcon("icons/cut.png"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        item.setActionCommand("Cut");
        item.addActionListener(this);

        mnuPopupMenu.add(item = new JMenuItem(new DefaultEditorKit.CutAction()));
        item.setText("Paste");
        item.setIcon(new ImageIcon("icons/paste.png"));

        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(this);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        item.setActionCommand("Past");

        mnuPopupMenu.setLabel("Justification");
        mnuPopupMenu.setBorder(new BevelBorder(BevelBorder.RAISED));
        mnuPopupMenu.addPopupMenuListener(new PopupPrintListener());

        mnuTree.add(item = new JMenuItem(new DefaultEditorKit.DefaultKeyTypedAction()));
        item.setText("New File");
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(this);

        mnuTree.add(item = new JMenuItem(new DefaultEditorKit.DefaultKeyTypedAction()));
        item.setText("New Folder");
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(this);

        mnuTree.add(item = new JMenuItem(new DefaultEditorKit.DefaultKeyTypedAction()));
        item.setText("Delete");
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(this);

        mnuTree.setLabel("Justification");
        mnuTree.setBorder(new BevelBorder(BevelBorder.RAISED));
        mnuTree.addPopupMenuListener(new PopupPrintListener());

        //
        //
        // jTextArea_Code.addMouseListener(new MousePopupListener())
        jMenuSource.add(item = new JMenuItem("Font"));
        item.addActionListener(this);

        jMenu.add(jMenuFile);
        jMenu.add(jMenuEdit);
        jMenu.add(jMenuRun);
        jMenu.add(jMenuSource);
        jMenu.add(jAboutMenu);
        this.setJMenuBar(jMenu);
        try {
            treeProject = new TreeProject(TreeProject.getConfigContent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        root = treeProject.getProjectRoot();
        jTreeProject = new JTree(root);
        jTreeProject.addTreeSelectionListener(this);
        jTreeProject.addMouseListener(new TreeMousePopupListener());

        final StyleContext cont = StyleContext.getDefaultStyleContext();
        final AttributeSet attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.red);
        final AttributeSet attrType = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.blue);
        final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
        DefaultStyledDocument doc = new DefaultStyledDocument() {
            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offset);
                if (before < 0) {
                    before = 0;
                }
                int after = findFirstNonWordChar(text, offset + str.length());
                int wordL = before;
                int wordR = before;

                while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                        if (text.substring(wordL, wordR).matches("(\\W)*(" + key + ")")) {

                            setCharacterAttributes(wordL, wordR - wordL, attr, false);
                        } else {
                            setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
                        }
                        wordL = wordR;
                    }
                    wordR++;
                }
            }

            public void remove(int offs, int len) throws BadLocationException {
                super.remove(offs, len);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0) {
                    before = 0;
                }
                int after = findFirstNonWordChar(text, offs);

                if (text.substring(before, after).matches("(\\W)*(private|public|protected)")) {
                    setCharacterAttributes(before, after - before, attr, false);
                } else {
                    setCharacterAttributes(before, after - before, attrBlack, false);
                }
            }
        };

        jTextArea_Code = new JTextPane(doc);
        jTextArea_Code.addKeyListener(this);
        jTextArea_Code.setSelectedTextColor(Color.BLUE);
        jTextArea_Code.setEditable(false);
        jTextArea_Code.addMouseListener(new MousePopupListener());
        jTextArea_Code.getDocument().addUndoableEditListener(new MyUndoableEditListener());

        treeView = new JScrollPane(jTreeProject);
        jEditor = new JScrollPane(jTextArea_Code);
        jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        jSplitPane.setTopComponent(treeView);
        jSplitPane.setBottomComponent(jEditor);
        add(jSplitPane);
        this.addWindowFocusListener(this);
        pack();
    }

    private int findLastNonWordChar(String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    private int findFirstNonWordChar(String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }

    /**
     * *
     *
     * start print
     *
     *
     */
    private void print(String s) {
        StringReader sr = new StringReader(s);
        LineNumberReader lnr = new LineNumberReader(sr);
        Font typeface = new Font("Monospaced", Font.PLAIN, 12);
        Properties p = new Properties();
        PrintJob pjob = getToolkit().getPrintJob(this, "Print report", p);

        if (pjob != null) {
            Graphics pg = pjob.getGraphics();
            if (pg != null) {
                FontMetrics fm = pg.getFontMetrics(typeface);
                int margin = 20;
                int pageHeight = pjob.getPageDimension().height - margin;
                int fontHeight = fm.getHeight();
                int fontDescent = fm.getDescent();
                int curHeight = margin;

                String nextLine;
                pg.setFont(jTextArea_Code.getFont());

                try {
                    do {
                        nextLine = lnr.readLine();
                        if (nextLine != null) {
                            if ((curHeight + fontHeight) > pageHeight) { // New
                                // Page
                                pg.dispose();
                                pg = pjob.getGraphics();
                                curHeight = margin;
                            }

                            curHeight += fontHeight;

                            if (pg != null) {
                                pg.setFont(typeface);
                                pg.drawString(nextLine, margin, curHeight - fontDescent);
                            }
                        }
                    } while (nextLine != null);

                } catch (EOFException eof) {
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            pg.dispose();
        }
        if (pjob != null) {
            pjob.end();
        }
    }
    //end Print

    /**
     * * * Use To call OpenProject Form
     *
     */
    @SuppressWarnings("deprecation")
    private void OpenProject() {
        OpenProject openProject = new OpenProject();
        openProject.show();
        openProject.setVisible(true);
        jTextArea_Code.setVisible(true);
        add(openProject);
    } //End OpenProjectForm

    /**
     * **************
     *
     * Use For Open NewProject Form
     */
    @SuppressWarnings("deprecation")
    private void NewProject() {
        try {
            NewProject ff = new NewProject();
            ff.show();
            ff.setVisible(true);
            jTextArea_Code.setVisible(true);
            add(ff);
        } catch (IOException ex) {
            Logger.getLogger(MainIde.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * ************************
     * Initialize Component Of Main Form
     */
    private void init() throws IOException {
        File tmp = new File(TreeProject.getConfigContent());
        root = new TreeProject(tmp.getPath()).getProjectRoot();
        initComponents();

    }

    /**
     * *************
     * Run Main Form
     */
    public MainIde() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     * @throws IOException
     */
    public static void main(String args[]) throws IOException {
        MainIde ide;
        ide = new MainIde();
        ide.setVisible(true);
        ide.setExtendedState(ide.getExtendedState() | JFrame.MAXIMIZED_BOTH);

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * **
     *
     * Control mouse Event on JTextPane (jTextArea_Code
     */
    class MousePopupListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {

            checkPopup(e);

        }

        public void mouseClicked(MouseEvent e) {
            checkPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            checkPopup(e);
        }

        /**
         * ***********
         *
         * Show pop menu when right click on mouse
         */
        private void checkPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                mnuPopupMenu.show(jTextArea_Code, e.getX(), e.getY());

            }
        }
    }

    /**
     * **********************
     * Control Mouse Event on jTreeProject
     */
    class TreeMousePopupListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {

            checkPopup(e);

        }

        public void mouseClicked(MouseEvent e) {
            checkPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            checkPopup(e);
        }

        /**
         * *
         *
         * Show Menu on Tree when right click on JTree
         */
        private void checkPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                mnuTree.show(jTreeProject, e.getX(), e.getY());

            }
        }
    }

    // An inner class to show when popup events occur
    class PopupPrintListener implements PopupMenuListener {

        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            System.out.println("Popup menu will be visible!");
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            System.out.println("Popup menu will be invisible!");
        }

        public void popupMenuCanceled(PopupMenuEvent e) {
            System.out.println("Popup menu is hidden!");
        }
    }

    public void save(String selectedFile) {
        File activeFile = new File(selectedFile);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(activeFile));
            writer.write(jTextArea_Code.getText());
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getActionCommand() == "Open Project") {
            OpenProject();
        } else if (arg0.getActionCommand() == "New Project") {
            NewProject();
        } else if (arg0.getActionCommand() == "Save") {
            save(selectedFile);
        } else if (arg0.getActionCommand() == "Run Project") {

        } else if (arg0.getActionCommand() == "Exit") {
            System.exit(0);
        } else if (arg0.getActionCommand() == "Format") {

        } else if (arg0.getActionCommand() == "About Software") {
            new About().software();
        } else if (arg0.getActionCommand() == "About Us") {
            new About().me();
        } else if (arg0.getActionCommand() == "Find") {
            Find find = new Find(jTextArea_Code);

        } else if (arg0.getActionCommand() == "Copy") {
            System.out.println(text);
        } else if (arg0.getActionCommand() == "Past") {
            jTextArea_Code.paste();
            System.out.println(text);
        } else if (arg0.getActionCommand() == "Cut") {
            jTextArea_Code.cut();
        } else if (arg0.getActionCommand() == "Remove") {
            jTextArea_Code.replaceSelection("");
        } else if (arg0.getActionCommand() == "Print") {
            print(jTextArea_Code.getText());
        } else if (arg0.getActionCommand() == "Font") {
            fontS.setVisible(true);
            fontS.okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    Font selectedFont = fontS.returnFont();
                    jTextArea_Code.setFont(selectedFont);
                    fontS.setVisible(false);
                }
            });

            fontS.cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    fontS.setVisible(false);
                }
            });
        } else if (arg0.getActionCommand() == "New File") {
            String args = jTreeProject.getLeadSelectionPath().toString();
            args = getThisNodePath(args);
            selectedFile = TreeProject.getFullPathProject(args);
            selectedFile += "\\" + JOptionPane.showInputDialog("Enter File Name");
            File tmp = new File(selectedFile);
            if (!tmp.exists()) {
                try {
                    tmp.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "This File Have Already!");
            }
        } else if (arg0.getActionCommand() == "New Folder") {
            String args = jTreeProject.getLeadSelectionPath().toString();
            args = getThisNodePath(args);
            selectedFile = TreeProject.getFullPathProject(args);
            selectedFile += "\\" + JOptionPane.showInputDialog("Enter Folder Name");
            File tmp = new File(selectedFile);
            if (tmp.exists()) {
                JOptionPane.showMessageDialog(this, "This Folder Name Have Already");
            } else {
                if (!tmp.mkdir()) {
                    JOptionPane.showMessageDialog(this, "Can not create this folder name here");
                }
            }
        } else if (arg0.getActionCommand() == "Delete") {

            /**
             * *
             *
             * Use For Delete File or Folder in Tree Directory
             */
            int conf = JOptionPane.showConfirmDialog(this, "Are you sure to delete this file");
            if (conf == JOptionPane.OK_OPTION) {
                String args = jTreeProject.getLeadSelectionPath().toString();
                args = getThisNodePath(args);
                selectedFile = TreeProject.getFullPathProject(args);
                File tmp = new File(selectedFile);
                tmp.delete();
                if (tmp.exists()) {
                    JOptionPane.showMessageDialog(this, "Can not Remove this file");
                } else {
                    JOptionPane.showMessageDialog(this, "File was Remove");
                }
            }
        }
    }

    @Override
    public void treeNodesChanged(TreeModelEvent arg0) {
        JOptionPane.showMessageDialog(this, arg0.getPath());

    }

    @Override
    public void treeNodesInserted(TreeModelEvent arg0) {

    }

    @Override
    public void treeNodesRemoved(TreeModelEvent arg0) {

    }

    @Override
    public void treeStructureChanged(TreeModelEvent arg0) {

    }

    @Override
    public void windowGainedFocus(WindowEvent e) {
      //  DefaultMutableTreeNode tmp = new DefaultMutableTreeNode(root);;
        if (last_state == 1) {
            try {
                root = new TreeProject(TreeProject.getConfigContent()).getProjectRoot();

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            
          //  if (!tmp.toString().equals(root.toString())) {
                
                jTreeProject = new JTree(root);
                jTreeProject.addTreeSelectionListener(this);
                jTreeProject.addMouseListener(new TreeMousePopupListener());
                treeView = new JScrollPane(jTreeProject);
                jSplitPane.setTopComponent(treeView);
                
            //}
         
        }
        last_state = 0;
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        last_state = 1;
    }

    public String getThisNodePath(String args) {
        args = args.replace('[', '\0');
        args = args.replace(']', '\0');
        String[] arg = args.split(", ");
        args = "\0";
        for (int i = 1; i < arg.length; i++) {
            args += "\\" + arg[i].trim();
        }
        args = args.trim();
        return args;
    }

    //Method for Tree Selection 
    public void valueChanged(TreeSelectionEvent arg0) {
        // save(lastSelectedFile);
        String args = arg0.getNewLeadSelectionPath().toString();
        args = getThisNodePath(args);
        String tmp = selectedFile;
        selectedFile = TreeProject.getFullPathProject(args);
        File file = new File(selectedFile);
        if (file.isFile()) {

            String content = "";
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // process the line.
                    content += line + "\n";
                }
                br.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            jTextArea_Code.setText(content);
            jTextArea_Code.setEditable(true);
        } else {
            selectedFile = tmp;
        }
    }

    // CLASS FOR UNDOLISTENER
    public class MyUndoableEditListener implements UndoableEditListener {

        public void undoableEditHappened(UndoableEditEvent e) {
            // Remember the edit and update the menus
            undo.addEdit(e.getEdit());
            undoAction.update();
            redoAction.update();
        }
    }

    class UndoAction extends AbstractAction {

        public UndoAction() {
            super("Undo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undo.undo();
            } catch (CannotUndoException ex) {
                System.out.println("Unable to undo: " + ex);
                ex.printStackTrace();
            }
            update();
            redoAction.update();
        }

        protected void update() {
            if (undo.canUndo()) {
                setEnabled(true);
                putValue("Undo", undo.getUndoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Undo");
            }
        }
    }

    class RedoAction extends AbstractAction {

        public RedoAction() {
            super("Redo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undo.redo();
            } catch (CannotRedoException ex) {
                System.out.println("Unable to redo: " + ex);
                ex.printStackTrace();
            }
            update();
            undoAction.update();
        }

        protected void update() {
            if (undo.canRedo()) {
                setEnabled(true);
                putValue("Redo", undo.getRedoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
        }
    }

}

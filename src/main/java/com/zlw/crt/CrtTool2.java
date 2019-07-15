package com.zlw.crt;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;


public class CrtTool2 {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				MyFrame myFrame =new MyFrame();
				myFrame.setExtendedState(2);
				myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				myFrame.setResizable(false);
				myFrame.setVisible(true);
			}
		});
	}
}

class PathPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JButton jbName;
	private JTextField jtf = new JTextField();
	private JFileChooser jfc = new JFileChooser();
	private JFileChooser jfc2 = new JFileChooser();
	FileNameExtensionFilter filterEXE = new FileNameExtensionFilter("exe文件","exe");
	private JFrame f  = new JFrame();
	private String path = null;
	private String labelName = null;

	public PathPanel(String title,String path){
		this.path = path;

		setLayout(new BorderLayout(2,2));
		JLabel jlName = new JLabel(title);
		add(jlName,BorderLayout.PAGE_START);

		jtf.setEditable(true);
		jtf.setText(MyUtil.output(path));
		jtf.setBorder(BorderFactory.createEtchedBorder());
		add(jtf,BorderLayout.CENTER);

		ActionListener listener = new FontAction(title);
		jbName = new JButton("Browse");
		jbName.addActionListener(listener);
		add(jbName,BorderLayout.EAST);

	}

	public String getPath() {
		return jtf.getText();
	}

	private class FontAction implements ActionListener {
		public FontAction(String labelN){
			labelName = labelN;
		}

		public void actionPerformed(ActionEvent eve) {
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			jfc2.setFileFilter(filterEXE);

			if(eve.getSource() == jbName) {
				if(labelName == "Log path:"){
					if(jfc.showOpenDialog(f) == JFileChooser.APPROVE_OPTION){
						String path1 = jfc.getSelectedFile().getAbsolutePath();
						jtf.setText(path1);
						String logPath = jtf.getText();
						MyUtil.input(path,logPath);
					}
				}

				if(labelName == "SecureCRT.exe path:"){
					if(jfc2.showOpenDialog(f) == JFileChooser.APPROVE_OPTION){
						String path2 = jfc2.getSelectedFile().getAbsolutePath();
						jtf.setText(path2);
						String crtPath = jtf.getText();
						MyUtil.input(path,crtPath);
					}
				}
			}
		}
	}

}

class MyFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_WIDTH = 340;
	public static final int DEFAULT_HEIGT = 372;
	private PathPanel crtPanel;
	private String add = "10.122.2.249";

	private JPanel serverPanel,p4;
	private JComboBox servers = new JComboBox();
	private JLabel serverLabel = new JLabel("选择服务器");
	private JButton ok = new JButton("连接");

	private File directory = new File("C:/CRT/scripts");
	private String crtPath = null;

	public MyFrame() {
		try {
			MyUtil.createTxt();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		setTitle("CrtTool");
		setLocation(DEFAULT_WIDTH, DEFAULT_HEIGT);
		setSize(DEFAULT_WIDTH,DEFAULT_HEIGT);

		setLayout(null);
		//CRT path
		crtPanel = new PathPanel("SecureCRT.exe path:","C:/CRT/CrtPath.txt");
		crtPath = crtPanel.getPath();
		crtPanel.setBounds(2,8,328,42);
		add(crtPanel);

		//servers列表
		servers.addItem("root@10.112.101.87");
		servers.addItem("root@10.112.68.87");
		servers.addItem("root@10.112.101.161");
		servers.addItem("root@10.112.101.165");
		servers.addItem("root@10.112.68.90");
		servers.addItem("root@10.122.2.248");
		servers.addItem("root@10.122.2.249");
		servers.addItem("root@10.112.101.129");
		servers.addItem("root@10.112.101.138");
		servers.addItem("root@10.112.101.127");
		servers.addItem("root@10.112.101.170");
		servers.addItem("root@10.112.68.200");
		servers.addItem("root@10.112.101.169");
		servers.addItem("root@10.128.16.218");
		servers.addItem("root@10.112.68.91");
		servers.addItem("root@10.112.68.115");
		servers.addItem("root@10.122.1.240");

		serverPanel = new JPanel();
		serverPanel.setLayout(new BorderLayout(5,2));
		serverPanel.add(serverLabel,BorderLayout.WEST);
		serverPanel.add(servers,BorderLayout.CENTER);
		serverPanel.setBounds(2,60,330,22);
		add(serverPanel);

		p4 = new JPanel();
		p4.setLayout(new BorderLayout(5,2));
		p4.setBounds(135,302,60,22);
		p4.add(ok,BorderLayout.CENTER);
		add(p4);

		ActionListener listener = new MyFrameAction();
		ok.addActionListener(listener);
	}


	private class MyFrameAction implements ActionListener  {
		public void actionPerformed(ActionEvent eve) {
			if(eve.getSource() == ok) {
				if( crtPanel.getPath()==null){
					JOptionPane.showMessageDialog(null,"ERROR! The path is null","Path error",JOptionPane.ERROR_MESSAGE);
				}
				else if(add.matches("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.)"
						+ "{3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))")) {
					String conn="# $language = \"VBScript\""+"\n";
					conn+="# $interface = \"1.0\""+"\n";
					conn+="Sub Main"+"\n";
					conn+="' Display SecureCRT's version"+"\n";
					String Str_A = servers.getSelectedItem().toString();
					conn+="crt.Screen.Send \"ssh "+Str_A+"\" & VbCr"+"\n";
					conn+="End Sub";
					MyUtil.input("C:/CRT/conn.vbs",conn);
					crtPath = crtPanel.getPath();
					MyUtil.openCRT("conn.vbs",crtPath,add);
				} else {
					JOptionPane.showMessageDialog(null,"ERROR! Please check your address","Address type error",JOptionPane.ERROR_MESSAGE);
				}
			}
		}

	}

	//文件过滤器，以后缀名不同过滤
	public class FileNameSelector implements FilenameFilter {
		String extension = ".";
		public FileNameSelector(String fileExtensionNoDot){
			extension += fileExtensionNoDot;
		}
		public boolean accept(File dir, String name){
			return name.endsWith(extension);
		}
	}

	private CheckableItem[] createData(String[] strs) {
		int n = strs.length;
		CheckableItem[] items = new CheckableItem[n];
		for (int i = 0; i < n; i++) {
			items[i] = new CheckableItem(strs[i]);
		}
		return items;
	}


	//已阅，jlist中名称，是否被选中，图标
	class CheckableItem {
		private String str;

		private boolean isSelected;

		private Icon icon;

		public CheckableItem(String str) {
			this.str = str;
			isSelected = false;
		}

		public void setSelected(boolean b) {
			isSelected = b;
		}

		public boolean isSelected() {
			return isSelected;
		}

		public String toString() {
			return str;
		}

		public void setIcon(Icon icon) {
			this.icon = icon;
		}

		public Icon getIcon() {
			return icon;
		}
	}

	class CheckListRenderer extends CheckRenderer implements ListCellRenderer {
//	    Icon commonIcon;

		public CheckListRenderer() {
			check.setBackground(UIManager.getColor("List.textBackground"));
			label.setForeground(UIManager.getColor("List.textForeground"));
//	      commonIcon = UIManager.getIcon("Tree.leafIcon");
		}

		public Component getListCellRendererComponent(JList list, Object value,
													  int index, boolean isSelected, boolean hasFocus) {
			setEnabled(list.isEnabled());
			check.setSelected(((CheckableItem) value).isSelected());
			label.setFont(list.getFont());
			label.setText(value.toString());
			label.setSelected(isSelected);
			label.setFocus(hasFocus);
//	      Icon icon = ((CheckableItem) value).getIcon();
//	      if (icon == null) {
//	        icon = commonIcon;
//	      }
//	      label.setIcon(icon);
			return this;
		}
	}

}

class CheckRenderer extends JPanel implements TreeCellRenderer {
	protected JCheckBox check;

	protected TreeLabel label;

	public CheckRenderer() {
		setLayout(null);
		add(check = new JCheckBox());
		add(label = new TreeLabel());
		check.setBackground(UIManager.getColor("Tree.textBackground"));
		label.setForeground(UIManager.getColor("Tree.textForeground"));
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
												  boolean isSelected, boolean expanded, boolean leaf, int row,
												  boolean hasFocus) {
		String stringValue = tree.convertValueToText(value, isSelected,
				expanded, leaf, row, hasFocus);
		setEnabled(tree.isEnabled());
		check.setSelected(((CheckNode) value).isSelected());
		label.setFont(tree.getFont());
		label.setText(stringValue);
		label.setSelected(isSelected);
		label.setFocus(hasFocus);
		if (leaf) {
			label.setIcon(UIManager.getIcon("Tree.leafIcon"));
		} else if (expanded) {
			label.setIcon(UIManager.getIcon("Tree.openIcon"));
		} else {
			label.setIcon(UIManager.getIcon("Tree.closedIcon"));
		}
		return this;
	}

	public Dimension getPreferredSize() {
		Dimension d_check = check.getPreferredSize();
		Dimension d_label = label.getPreferredSize();
		return new Dimension(d_check.width + d_label.width,
				(d_check.height < d_label.height ? d_label.height
						: d_check.height));
	}

	public void doLayout() {
		Dimension d_check = check.getPreferredSize();
		Dimension d_label = label.getPreferredSize();
		int y_check = 0;
		int y_label = 0;
		if (d_check.height < d_label.height) {
			y_check = (d_label.height - d_check.height) / 2;
		} else {
			y_label = (d_check.height - d_label.height) / 2;
		}
		check.setLocation(0, y_check);
		check.setBounds(0, y_check, d_check.width, d_check.height);
		label.setLocation(d_check.width, y_label);
		label.setBounds(d_check.width, y_label, d_label.width, d_label.height);
	}

	public void setBackground(Color color) {
		if (color instanceof ColorUIResource)
			color = null;
		super.setBackground(color);
	}

	public class TreeLabel extends JLabel {
		boolean isSelected;

		boolean hasFocus;

		public TreeLabel() {
		}

		public void setBackground(Color color) {
			if (color instanceof ColorUIResource)
				color = null;
			super.setBackground(color);
		}

		public void paint(Graphics g) {
			String str;
			if ((str = getText()) != null) {
				if (0 < str.length()) {
					if (isSelected) {
						g.setColor(UIManager
								.getColor("Tree.selectionBackground"));
					} else {
						g.setColor(UIManager.getColor("Tree.textBackground"));
					}
					Dimension d = getPreferredSize();
					int imageOffset = 0;
					Icon currentI = getIcon();
					if (currentI != null) {
						imageOffset = currentI.getIconWidth()
								+ Math.max(0, getIconTextGap() - 1);
					}
					g.fillRect(imageOffset, 0, d.width - 1 - imageOffset,
							d.height);
					if (hasFocus) {
//	            g.setColor(UIManager
//	                .getColor("Tree.selectionBorderColor"));
						g.drawRect(imageOffset, 0, d.width - 1 - imageOffset,
								d.height - 1);
					}
				}
			}
			super.paint(g);
		}

		public Dimension getPreferredSize() {
			Dimension retDimension = super.getPreferredSize();
			if (retDimension != null) {
				retDimension = new Dimension(retDimension.width + 3,
						retDimension.height);
			}
			return retDimension;
		}

		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}

		public void setFocus(boolean hasFocus) {
			this.hasFocus = hasFocus;
		}
	}
}

class CheckNode extends DefaultMutableTreeNode {

	public final static int SINGLE_SELECTION = 0;

	public final static int DIG_IN_SELECTION = 4;

	protected int selectionMode;

	protected boolean isSelected;

	public CheckNode() {
		this(null);
	}

	public CheckNode(Object userObject) {
		this(userObject, true, false);
	}

	public CheckNode(Object userObject, boolean allowsChildren,
					 boolean isSelected) {
		super(userObject, allowsChildren);
		this.isSelected = isSelected;
		setSelectionMode(DIG_IN_SELECTION);
	}

	public void setSelectionMode(int mode) {
		selectionMode = mode;
	}

	public int getSelectionMode() {
		return selectionMode;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;

		if ((selectionMode == DIG_IN_SELECTION) && (children != null)) {
			Enumeration e = children.elements();
			while (e.hasMoreElements()) {
				CheckNode node = (CheckNode) e.nextElement();
				node.setSelected(isSelected);
			}
		}
	}

	public boolean isSelected() {
		return isSelected;
	}
}








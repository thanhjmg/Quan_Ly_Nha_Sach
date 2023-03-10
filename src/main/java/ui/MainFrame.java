package ui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import entity.NhanVien;

import javax.swing.UIManager.LookAndFeelInfo;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static NhanVien nhanVien;
	private JPanel selectedPanel = null;
	private JPanel pnlCenterOfCenter;
	
	private QuanLyHoaDonPanel quanLyHoaDonPanel;
	private QuanLyNhanVienPanel quanLyNhanVienPanel;
	private QuanLyKhachHangPanel quanLyKhachHangPanel;
	private QuanLySanPhamPanel quanLySanPhamPanel;
	private QuanLyNhaCungCapPanel quanLyNhaCungCapPanel;

	private DangNhapFrame dangNhapFrame;
	private ColoredButton btnDangXuat;
	private ThongKePanel thongKePanel;
	private AboutDialog aboutDialog;
	private TrangChuPanel trangChuPanel;
	private ColoredButton btnHelp;
	private ColoredButton btnAbout;
	private ColoredButton btnMenu;
	private JScrollPane scrollMenu;
	private JPanel pnlMenuTC;
	private JPanel pnlMenuQLHD;
	private JPanel pnlMenuQLKH;
	private JPanel pnlMenuQLNV;
	private JPanel pnlMenuQLSP;
	private JPanel pnlMenuQLNCC;
	private JPanel pnlMenuQLTK;


	public MainFrame(NhanVien nhanVien) {
		MainFrame.nhanVien = nhanVien;

		setLookAndFeel();

		setSize(900, 700);
		setExtendedState(MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		///////////////
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int act = JOptionPane.showConfirmDialog(MainFrame.this, "B???n c?? ch???c ch???n mu???n tho??t?", "X??c nh???n", JOptionPane.OK_CANCEL_OPTION);
			
				if(act == JOptionPane.OK_OPTION)
					System.exit(0);
			}
		});
		getContentPane().setBackground(Color.white);

		JPanel panel = new JPanel(new BorderLayout());
		getContentPane().add(panel);

		panel.add(Box.createHorizontalStrut(10), BorderLayout.EAST);

		panel.setOpaque(true);
		panel.setBackground(Color.white);

		JPanel pnlMain = new JPanel(new BorderLayout());
		JScrollPane scroll = new JScrollPane(pnlMain);
		scroll.setOpaque(false);
		scroll.setBorder(null);
		scroll.getViewport().setOpaque(false);
		panel.add(scroll);

		pnlMain.setOpaque(false);
		pnlMain.add(Box.createHorizontalStrut(10), BorderLayout.WEST);
		pnlMain.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);

		JPanel pnlNorth;
		pnlMain.add(pnlNorth = new JPanel(), BorderLayout.NORTH);

		pnlNorth.setLayout(new BoxLayout(pnlNorth, BoxLayout.X_AXIS));

		addNorth(pnlNorth);

		JPanel pnlCenter;
		pnlMain.add(pnlCenter = new JPanel(new BorderLayout()), BorderLayout.CENTER);
		pnlCenter.setOpaque(false);
		pnlCenter.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, UIConstant.LINE_COLOR));

		pnlCenter.add(pnlCenterOfCenter = new JPanel(new BorderLayout()), BorderLayout.CENTER);
		pnlCenterOfCenter.setOpaque(false);

		JPanel pnlMenu = new JPanel();
		pnlMenu.setLayout(new BoxLayout(pnlMenu, BoxLayout.Y_AXIS));
		pnlMenu.setOpaque(false);

		scrollMenu = new JScrollPane(pnlMenu);
		scrollMenu.getViewport().setBackground(Color.white);
		scrollMenu.setBorder(null);
		pnlMain.add(scrollMenu, BorderLayout.WEST);

		addMenu(pnlMenu);

		addShortCutKey(getAllComponents(this));

		trangChuPanel = new TrangChuPanel(this);
		trangChuPanel.setPreferredSize(new Dimension(700, 600));
		addShortCutKey(getAllComponents(trangChuPanel));

		quanLySanPhamPanel = new QuanLySanPhamPanel(this);
		addShortCutKey(getAllComponents(quanLySanPhamPanel));

		quanLyHoaDonPanel = new QuanLyHoaDonPanel(this);
		addShortCutKey(getAllComponents(quanLyHoaDonPanel));

		quanLyNhanVienPanel = new QuanLyNhanVienPanel(this);
		addShortCutKey(getAllComponents(quanLyNhanVienPanel));

		quanLyKhachHangPanel = new QuanLyKhachHangPanel(this);
		addShortCutKey(getAllComponents(quanLyKhachHangPanel));

		quanLyNhaCungCapPanel = new QuanLyNhaCungCapPanel(this);
		addShortCutKey(getAllComponents(quanLyNhaCungCapPanel));

		thongKePanel = new ThongKePanel(this);
		addShortCutKey(getAllComponents(thongKePanel));

		changeCenter(trangChuPanel);

		quanLyKhachHangPanel.setPreferredSize(trangChuPanel.getPreferredSize());
		quanLyHoaDonPanel.setPreferredSize(trangChuPanel.getPreferredSize());
		quanLyNhanVienPanel.setPreferredSize(trangChuPanel.getPreferredSize());
		quanLySanPhamPanel.setPreferredSize(trangChuPanel.getPreferredSize());
		quanLyNhaCungCapPanel.setPreferredSize(trangChuPanel.getPreferredSize());
		thongKePanel.setPreferredSize(trangChuPanel.getPreferredSize());
	}

	private void addShortCutKey(List<Component> components) {
		components.forEach(item -> {
			item.addKeyListener(new KeyAdapter() {
				private boolean isAltPressed = false;
				private Object pnlMenuQLNV;

				@Override
				public void keyPressed(KeyEvent e) {
					if(isAltPressed) {
						//Nh???n ph??m L khi ??ang gi??? ph??m Ctrl
						if(e.getKeyCode() == KeyEvent.VK_H)
							changeCenter(trangChuPanel);
						else if(e.getKeyCode() == KeyEvent.VK_O)
							changeCenter(quanLyHoaDonPanel);
						else if(e.getKeyCode() == KeyEvent.VK_C)
							changeCenter(quanLyKhachHangPanel);
						else if(e.getKeyCode() == KeyEvent.VK_E && pnlMenuQLNV != null)
							changeCenter(quanLyNhanVienPanel);
						else if(e.getKeyCode() == KeyEvent.VK_S)
							changeCenter(quanLyNhaCungCapPanel);
						else if(e.getKeyCode() == KeyEvent.VK_M)
							changeCenter(quanLyKhachHangPanel);
						else if(e.getKeyCode() == KeyEvent.VK_M)
							changeCenter(quanLySanPhamPanel);
						else if(e.getKeyCode() == KeyEvent.VK_R)
							changeCenter(thongKePanel);

					} else if(e.getKeyCode() == KeyEvent.VK_ALT)
						isAltPressed = true;
					else 
						isAltPressed = false;
				}

				@Override
				public void keyReleased(KeyEvent e) {
					isAltPressed = false;
				}
			});
		});
	}
	private List<Component> getAllComponents(Container c) {
		Component[] comps = c.getComponents();
		List<Component> compList = new ArrayList<Component>();
		for (Component comp : comps) {
			compList.add(comp);
			if (comp instanceof Container)
				compList.addAll(getAllComponents((Container) comp));
		}
		return compList;
	}

	public static NhanVien getNhanVien() {
		return nhanVien;
	}

	public QuanLyHoaDonPanel getQuanLyHoaDonPanel() {
		return quanLyHoaDonPanel;
	}

	public QuanLyNhanVienPanel getQuanLyNhanVienPanel() {
		return quanLyNhanVienPanel;
	}

	public QuanLyKhachHangPanel getQuanLyKhachHangPanel() {
		return quanLyKhachHangPanel;
	}

	public QuanLySanPhamPanel getQuanLyThuocPanel() {
		return quanLySanPhamPanel;
	}

	public QuanLyNhaCungCapPanel getQuanLyNhaCungCapPanel() {
		return quanLyNhaCungCapPanel;
	}

	public ThongKePanel getThongKePanel() {
		return thongKePanel;
	}

	public TrangChuPanel getTrangChuPanel() {
		return trangChuPanel;
	}

	private void addMenu(JPanel pnlMenu) {
		if(nhanVien != null) {
			Box boxTaiKhoan = Box.createVerticalBox();
			boxTaiKhoan.setOpaque(true);
			boxTaiKhoan.setBackground(new Color(0xE7E7E7));
			pnlMenu.add(boxTaiKhoan);

			btnDangXuat = new ColoredButton("????ng xu???t");
			btnDangXuat.setBackground(UIConstant.DANGER_COLOR);
			btnDangXuat.setFont(UIConstant.NORMAL_FONT);

			btnDangXuat.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(dangNhapFrame == null)
						dangNhapFrame = new DangNhapFrame();

					MainFrame.this.setVisible(false);
					dangNhapFrame.setVisible(true);
				}
			});

			Box boxHinh = Box.createHorizontalBox();
			boxHinh.add(Box.createHorizontalGlue());
			boxHinh.add(new JLabel(new ImageIcon("img/staff.png")));
			boxHinh.add(Box.createHorizontalGlue());

			JLabel lbTenNV = new JLabel(nhanVien.getTenNhanVien());
			lbTenNV.setFont(UIConstant.NORMAL_FONT);

			Box boxTen = Box.createHorizontalBox();
			boxTen.add(Box.createHorizontalGlue());
			boxTen.add(lbTenNV);
			boxTen.add(Box.createHorizontalGlue());

			Box boxDangXuat = Box.createHorizontalBox();
			boxDangXuat.add(Box.createHorizontalGlue());
			boxDangXuat.add(btnDangXuat);
			boxDangXuat.add(Box.createHorizontalGlue());

			boxTaiKhoan.add(boxHinh, BorderLayout.NORTH);
			boxTaiKhoan.add(boxTen, BorderLayout.CENTER);
			boxTaiKhoan.add(Box.createVerticalStrut(3));
			boxTaiKhoan.add(boxDangXuat, BorderLayout.SOUTH);
			boxTaiKhoan.add(Box.createVerticalStrut(3));
		}

		pnlMenu.setOpaque(false);
		pnlMenuTC = addMenuItem(pnlMenu, "Trang ch???", "img/trangchu2.png");
		pnlMenuTC.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				changeCenter(trangChuPanel);
			}
		});
		pnlMenuQLHD = addMenuItem(pnlMenu, "Qu???n L?? H??a ????n", "img/hoadon.png");
		pnlMenuQLHD.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				changeCenter(quanLyHoaDonPanel);
			}
		});
		pnlMenuQLKH = addMenuItem(pnlMenu, "Qu???n l?? kh??ch h??ng", "img/khachhang.png");
		pnlMenuQLKH.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				changeCenter(quanLyKhachHangPanel);
			}
		});

		if(nhanVien.getLoaiNhanVien() == 0) {
			pnlMenuQLNV = addMenuItem(pnlMenu, "Qu???n l?? nh??n vi??n", "img/nhanvien.png");
			pnlMenuQLNV.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					changeCenter(quanLyNhanVienPanel);
				}
			});
		}
		
		pnlMenuQLSP = addMenuItem(pnlMenu, "Qu???n l?? S???n Ph???m", "img/sanpham.png");
		pnlMenuQLSP.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				changeCenter(quanLySanPhamPanel);
			}
		});
		pnlMenuQLNCC = addMenuItem(pnlMenu, "Qu???n l?? nh?? cung c???p", "img/nhacungcap.png");
		pnlMenuQLNCC.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				changeCenter(quanLyNhaCungCapPanel);
			}
		});
		pnlMenuQLTK = addMenuItem(pnlMenu, "Th???ng k??", "img/thongke.png");
		pnlMenuQLTK.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				changeCenter(thongKePanel);
			}
		});

	}

	private JPanel addMenuItem(JPanel pnlMenu, String name, String image) {
		JPanel panel = new JPanel(new BorderLayout());
		pnlMenu.add(panel);

		panel.setFocusable(true);
		panel.setOpaque(true);
		panel.setPreferredSize(new Dimension(140, 80));
		panel.setBackground(new Color(0xE7E7E7));
		panel.addMouseListener(new MouseAdapter() {
			private boolean isEntered = false;

			@Override
			public void mouseEntered(MouseEvent e) {
				JPanel panel = (JPanel) e.getSource();

				if(selectedPanel == panel)
					return;

				panel.setBackground(new Color(0xDDDDDD));
				isEntered = true;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				JPanel panel = (JPanel) e.getSource();

				if(selectedPanel == panel)
					return;

				panel.setBackground(new Color(0xE7E7E7));
				isEntered = false;
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton() != MouseEvent.BUTTON1)
					return;

				JPanel panel = (JPanel) e.getSource();
				panel.setBackground(new Color(0xFFFFFF));

				if(selectedPanel == panel)
					return;

				if(selectedPanel != null)
					selectedPanel.setBackground(new Color(0xE7E7E7));

				selectedPanel = panel;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if(isEntered) {
					JPanel panel = (JPanel) e.getSource();
					if(selectedPanel == panel)
						return;
					panel.setBackground(new Color(0xDDDDDD));
				}
			}
		});

		panel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					JPanel panel = (JPanel) e.getSource();
					panel.setBackground(new Color(0xFFFFFF));

					if(selectedPanel == panel)
						return;

					if(selectedPanel != null)
						selectedPanel.setBackground(new Color(0xE7E7E7));

					selectedPanel = panel;
				}
			}
		});

		panel.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				JPanel panel = (JPanel) e.getSource();

				if(selectedPanel == panel)
					return;

				panel.setBackground(new Color(0xE7E7E7));
			}

			@Override
			public void focusGained(FocusEvent e) {
				JPanel panel = (JPanel) e.getSource();

				if(selectedPanel == panel)
					return;

				panel.setBackground(new Color(0xDDDDDD));
			}
		});

		if(image != null || image != "") {
			panel.add(new JLabel(new ImageIcon(image)), BorderLayout.CENTER);
		}

		JLabel lbName;
		panel.add(lbName = new JLabel(name), BorderLayout.SOUTH);
		lbName.setForeground(new Color(0x4E4E4E));
		lbName.setFont(UIConstant.NORMAL_FONT);
		lbName.setHorizontalAlignment(JLabel.CENTER);

		return panel;
	}

	public JPanel getPanelCenter() {
		return pnlCenterOfCenter;
	}

	private void addNorth(JPanel pnlNorth) {
		pnlNorth.setOpaque(false);
		pnlNorth.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstant.LINE_COLOR));

		btnMenu = new ColoredButton(new ImageIcon("img/menu.png"));
		btnMenu.setBackground(Color.white);

		btnMenu.addActionListener((e) -> {
			scrollMenu.setVisible(!scrollMenu.isVisible());
			repaint();
			revalidate();
		});

		pnlNorth.add(Box.createHorizontalStrut(5));
		pnlNorth.add(btnMenu);
		pnlNorth.add(Box.createHorizontalStrut(5));

		JLabel lbsachhay;

		pnlNorth.add(new JLabel(new ImageIcon("img/logo2.jpg")));
		pnlNorth.add(lbsachhay = new JLabel("SachHay"));

		lbsachhay.setFont(new Font("Forte", Font.BOLD, 26));
		lbsachhay.setForeground(new Color(255,127,0));

		btnHelp = new ColoredButton("Help"); btnHelp.setBackground(Color.white); btnHelp.setForeground(Color.black);
		btnAbout = new ColoredButton("About"); btnAbout.setBackground(Color.white);  btnAbout.setForeground(Color.black);

		pnlNorth.add(Box.createHorizontalGlue());
		pnlNorth.add(btnAbout);
		pnlNorth.add(Box.createHorizontalStrut(10));
		pnlNorth.add(btnHelp);
		pnlNorth.add(Box.createHorizontalStrut(10));

		btnAbout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(aboutDialog == null)
					aboutDialog = new AboutDialog(MainFrame.this);

				aboutDialog.setVisible(true);
			}

		});
	}

	public void changeCenter(JPanel newPanel) {
		if(getPanelCenter().getComponentCount() > 0)
			if(getPanelCenter().getComponent(0).equals(newPanel))
				return;

		if(selectedPanel != null)
			selectedPanel.setBackground(new Color(0xE7E7E7));

		getPanelCenter().removeAll();
		getPanelCenter().add(newPanel);
		getPanelCenter().add(Box.createHorizontalStrut(10), BorderLayout.WEST);
		getPanelCenter().repaint();
		getPanelCenter().revalidate();

		if(newPanel.equals(quanLyHoaDonPanel)) {
			selectedPanel = pnlMenuQLHD;
			selectedPanel.setBackground(new Color(0xFFFFFF));
			quanLyHoaDonPanel.getDefaultFocusComponent().requestFocus();
		} else if(newPanel.equals(quanLyNhaCungCapPanel)) {
			selectedPanel = pnlMenuQLNCC;
			selectedPanel.setBackground(new Color(0xFFFFFF));
			quanLyNhaCungCapPanel.getDefaultFocusComponent().requestFocus();
		} else if(newPanel.equals(quanLyKhachHangPanel)) {
			selectedPanel = pnlMenuQLKH;
			selectedPanel.setBackground(new Color(0xFFFFFF));
			quanLyKhachHangPanel.getDefaultFocusComponent().requestFocus();
		} else if(newPanel.equals(quanLyNhanVienPanel)) {
			selectedPanel = pnlMenuQLNV;
			selectedPanel.setBackground(new Color(0xFFFFFF));
			quanLyNhanVienPanel.getDefaultFocusComponent().requestFocus();
		} else if(newPanel.equals(quanLySanPhamPanel)) {
			selectedPanel = pnlMenuQLSP;
			selectedPanel.setBackground(new Color(0xFFFFFF));
			quanLySanPhamPanel.getDefaultFocusComponent().requestFocus();
		} else if(newPanel.equals(thongKePanel)) {
			selectedPanel = pnlMenuQLTK;
			selectedPanel.setBackground(new Color(0xFFFFFF));
		} else if(newPanel.equals(trangChuPanel)) {
			selectedPanel = pnlMenuTC;
			selectedPanel.setBackground(new Color(0xFFFFFF));
		}
	}

	public void setDangNhapFrame(DangNhapFrame dangNhapFrame) {
		this.dangNhapFrame = dangNhapFrame;
	}

	private void setLookAndFeel() {
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			if (info.getName().equals("Windows")) {
				try {
					UIManager.setLookAndFeel(info.getClassName());
					break;

				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}
}

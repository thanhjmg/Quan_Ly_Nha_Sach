package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.hssf.record.cf.BorderFormatting;

import com.toedter.calendar.JDateChooser;

import dao.HoaDonDAO;
import entity.HoaDon;

public class QuanLyHoaDonPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ColoredButton btnLapHoaDon;
	private ColoredButton btnTimKiem;
	private DefaultTableModel modelHD;
	private CustomTable tableHoaDon;
	private ColoredButton btnQuayLai;
	private JTextField txtMaHoaDon;
	private JTextField txtTenKhachHang;
	private JTextField txtTenNhanVien;
	private JDateChooser dateNgayLap;
	private ColoredButton btnXoaTrang;
	private HoaDonDAO hoaDonDAO;
	private List<HoaDon> hoaDons = null;
	protected String dir = "";
	private MainFrame mainFrame;
	private ColoredButton btnXemHD;
	private ColoredButton btnHome;
	private ColoredButton btnEnd;
	private ColoredButton btnBefore;
	private ColoredButton btnNext;
	private int currentIndex = 0;
	private JLabel lbPage;

	public QuanLyHoaDonPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLookAndFeel();

		setOpaque(true);
		setBackground(Color.white);
		setLayout(new BorderLayout());

		//North

		JPanel pnlNorth;
		add(pnlNorth = new JPanel(new BorderLayout()), BorderLayout.NORTH);
		pnlNorth.setOpaque(false);

		JPanel pnlNorthAbove;
		pnlNorth.add(pnlNorthAbove = new JPanel(new BorderLayout()), BorderLayout.NORTH);
		pnlNorthAbove.setOpaque(false);
		pnlNorthAbove.add(Box.createVerticalStrut(10), BorderLayout.NORTH);
		pnlNorthAbove.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);

		JLabel lbOrder = new JLabel("Qu???n l?? h??a ????n");
		lbOrder.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lbOrder.setForeground(new Color(255,127,0));
		lbOrder.setHorizontalAlignment(JLabel.CENTER);

		pnlNorthAbove.add(lbOrder, BorderLayout.NORTH);

		JPanel pnlNorthBelow;
		pnlNorth.add(pnlNorthBelow = new JPanel(), BorderLayout.CENTER);
		pnlNorthBelow.setOpaque(false);
		pnlNorthBelow.setLayout(new BorderLayout());

		pnlNorthBelow.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(UIConstant.LINE_COLOR), "Nh???p th??ng tin c???n t??m"));

		Box boxTT = Box.createHorizontalBox();
		pnlNorthBelow.add(boxTT, BorderLayout.NORTH);

		Box boxLeft = Box.createVerticalBox();
		Box boxRight = Box.createVerticalBox();

		boxTT.add(boxLeft);
		boxTT.add(boxRight);

		Box boxMaHD = Box.createHorizontalBox();
		Box boxTenKH = Box.createHorizontalBox();
		Box boxTenNV = Box.createHorizontalBox();
		Box boxNgayLap = Box.createHorizontalBox();
		Box boxButtonInput = Box.createVerticalBox();

		boxButtonInput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, UIConstant.LINE_COLOR), "Ch???n t??c v???"));

		pnlNorth.add(boxButtonInput, BorderLayout.EAST);

		boxLeft.add(Box.createVerticalStrut(15));
		boxLeft.add(boxMaHD);
		boxLeft.add(Box.createVerticalStrut(15));
		boxLeft.add(boxTenKH);
		boxRight.add(Box.createVerticalStrut(15));
		boxRight.add(boxNgayLap);
		boxRight.add(Box.createVerticalStrut(15));
		boxRight.add(boxTenNV);
		boxRight.add(Box.createVerticalStrut(3));

		JLabel lbMaHD = new JLabel("M?? h??a ????n");
		JLabel lbTenKH = new JLabel("T??n kh??ch h??ng");
		JLabel lbTenNV = new JLabel("T??n nh??n vi??n");
		JLabel lbNgayLap = new JLabel("Ng??y l???p");

		lbMaHD.setPreferredSize(new Dimension(100, 20)); lbMaHD.setFont(UIConstant.NORMAL_FONT);
		lbTenKH.setPreferredSize(new Dimension(100, 20)); lbTenKH.setFont(UIConstant.NORMAL_FONT);
		lbTenNV.setPreferredSize(new Dimension(100, 20)); lbTenNV.setFont(UIConstant.NORMAL_FONT);
		lbNgayLap.setPreferredSize(new Dimension(100, 20)); lbNgayLap.setFont(UIConstant.NORMAL_FONT);
//
		txtMaHoaDon = new JTextField(); 
		txtMaHoaDon.setFont(UIConstant.NORMAL_FONT);
		txtTenKhachHang = new JTextField(25); txtTenKhachHang.setFont(UIConstant.NORMAL_FONT);
		txtTenNhanVien = new JTextField(25); txtTenNhanVien.setFont(UIConstant.NORMAL_FONT);
		dateNgayLap = new JDateChooser(); dateNgayLap.setFont(UIConstant.NORMAL_FONT);
		dateNgayLap.setDateFormatString("dd-MM-yyyy");
//		txtMaHoaDon.setBorder(new BorderFormatting().);
		boxMaHD.add(Box.createHorizontalStrut(5));
		boxMaHD.add(lbMaHD);
		boxMaHD.add(Box.createHorizontalStrut(5));
		boxMaHD.add(txtMaHoaDon);
		boxMaHD.add(Box.createHorizontalStrut(5));

		boxTenKH.add(Box.createHorizontalStrut(5));
		boxTenKH.add(lbTenKH);
		boxTenKH.add(Box.createHorizontalStrut(5));
		boxTenKH.add(txtTenKhachHang);
		boxTenKH.add(Box.createHorizontalStrut(5));

		boxTenNV.add(Box.createHorizontalStrut(5));
		boxTenNV.add(lbTenNV);
		boxTenNV.add(Box.createHorizontalStrut(5));
		boxTenNV.add(txtTenNhanVien);
		boxTenNV.add(Box.createHorizontalStrut(5));

		boxNgayLap.add(Box.createHorizontalStrut(5));
		boxNgayLap.add(lbNgayLap);
		boxNgayLap.add(Box.createHorizontalStrut(5));
		boxNgayLap.add(dateNgayLap);
		boxNgayLap.add(Box.createHorizontalStrut(5));

		btnTimKiem = new ColoredButton("T??m ki???m", new ImageIcon("img/search.png"));
		btnTimKiem.setFont(UIConstant.NORMAL_FONT);
		btnTimKiem.setBackground(UIConstant.PRIMARY_COLOR);
		btnTimKiem.setForeground(Color.white);
		btnTimKiem.setBorderRadius(30);
		btnLapHoaDon = new ColoredButton("L???p h??a ????n", new ImageIcon("img/add.png"));
		btnLapHoaDon.setForeground(Color.white);
		btnLapHoaDon.setBackground(UIConstant.PRIMARY_COLOR);
		btnLapHoaDon.setBorderRadius(30);
		btnXoaTrang = new ColoredButton("Xo?? tr???ng", new ImageIcon("img/empty.png"));
		btnXoaTrang.setBackground(UIConstant.PRIMARY_COLOR);
		btnXoaTrang.setBorderRadius(30);

		btnTimKiem.setPreferredSize(btnLapHoaDon.getPreferredSize());

		Box boxTK = Box.createHorizontalBox();
		Box boxLap = Box.createHorizontalBox();
		Box boxXT = Box.createHorizontalBox();
		boxTK.add(Box.createHorizontalGlue());
		boxTK.add(Box.createHorizontalStrut(5));
		boxTK.add(btnTimKiem);
		boxTK.add(Box.createHorizontalStrut(5));
		boxTK.add(Box.createHorizontalGlue());

		boxLap.add(Box.createHorizontalGlue());
		boxLap.add(Box.createHorizontalStrut(5));
		boxLap.add(btnLapHoaDon);
		boxLap.add(Box.createHorizontalStrut(5));
		boxLap.add(Box.createHorizontalGlue());
		
		boxXT.add(Box.createHorizontalGlue());
		boxXT.add(Box.createHorizontalStrut(5));
		boxXT.add(btnXoaTrang);
		boxXT.add(Box.createHorizontalStrut(5));
		boxXT.add(Box.createHorizontalGlue());

		boxButtonInput.add(Box.createVerticalGlue());
		boxButtonInput.add(Box.createVerticalStrut(5));
		boxButtonInput.add(boxTK);
		boxButtonInput.add(Box.createVerticalStrut(5));
		boxButtonInput.add(boxLap);
		boxButtonInput.add(Box.createVerticalStrut(5));
		boxButtonInput.add(Box.createVerticalGlue());
		boxButtonInput.add(Box.createVerticalStrut(5));
		boxButtonInput.add(boxXT);
		boxButtonInput.add(Box.createVerticalStrut(5));
		boxButtonInput.add(Box.createVerticalGlue());
		

		pnlNorthBelow.add(Box.createVerticalStrut(5), BorderLayout.CENTER);

		//Center

		JPanel pnlCenter;
		add(pnlCenter = new JPanel(new BorderLayout()));
		pnlCenter.setOpaque(false);
		pnlCenter.add(Box.createVerticalStrut(10), BorderLayout.NORTH);


		tableHoaDon = new CustomTable(modelHD = new DefaultTableModel(new String[] {"M?? h??a ????n", "Kh??ch h??ng", "Nh??n vi??n l???p", "Ng??y l???p", "T???ng ti???n"}, 0));
		tableHoaDon.setFont(UIConstant.NORMAL_FONT);
		
		JTabbedPane tabPaneHD = new JTabbedPane();
		tabPaneHD.setOpaque(false);

		pnlCenter.add(tabPaneHD, BorderLayout.CENTER);

		JScrollPane scroll = new JScrollPane(tableHoaDon);
		scroll.getViewport().setBackground(Color.white);
		scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		tabPaneHD.addTab("Danh s??ch h??a ????n", scroll);
		
		lbPage = new JLabel("Trang 1 trong 1 trang."); lbPage.setFont(UIConstant.NORMAL_FONT);

		btnHome = new ColoredButton(new ImageIcon("img/double_left.png")); btnHome.setBackground(UIConstant.LINE_COLOR);
		btnEnd = new ColoredButton(new ImageIcon("img/double_right.png")); btnEnd.setBackground(UIConstant.LINE_COLOR);
		btnBefore = new ColoredButton(new ImageIcon("img/left.png")); btnBefore.setBackground(UIConstant.LINE_COLOR);
		btnNext = new ColoredButton(new ImageIcon("img/right.png")); btnNext.setBackground(UIConstant.LINE_COLOR);

		btnHome.setToolTipText("Trang ?????u");
		btnHome.setBorderRadius(30);
		btnEnd.setToolTipText("Trang cu???i");
		btnEnd.setBorderRadius(30);
		btnBefore.setToolTipText("Trang tr?????c");
		btnBefore.setBorderRadius(30);
		btnNext.setToolTipText("Trang k??? ti???p");
		btnNext.setBorderRadius(30);
		
		Box boxPage = Box.createHorizontalBox();
		boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnHome); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnBefore); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnNext); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnEnd); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(Box.createHorizontalGlue());
		boxPage.add(lbPage); boxPage.add(Box.createHorizontalStrut(5));

		pnlCenter.add(boxPage, BorderLayout.SOUTH);

		//South

		JPanel pnlSouth = new JPanel(new BorderLayout());
		add(pnlSouth, BorderLayout.SOUTH);
		pnlSouth.setOpaque(false);
		pnlSouth.add(Box.createVerticalStrut(5), BorderLayout.NORTH);
		pnlSouth.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);

		btnQuayLai = new ColoredButton("Quay l???i", new ImageIcon("img/back.png"));
		btnQuayLai.setBorderRadius(30);
		btnQuayLai.setBackground(UIConstant.PRIMARY_COLOR);

		btnXemHD = new ColoredButton("Xem h??a ????n", new ImageIcon("img/detail.png"));
		btnXemHD.setBorderRadius(30);
		btnXemHD.setBackground(UIConstant.PRIMARY_COLOR);

		Box boxButton;
		pnlSouth.add(boxButton = Box.createHorizontalBox(), BorderLayout.CENTER);

		boxButton.add(Box.createHorizontalStrut(1060));
		boxButton.add(btnXemHD);
		boxButton.add(Box.createHorizontalStrut(50));
		boxButton.add(btnQuayLai);
		boxButton.add(Box.createHorizontalStrut(10));

		addEvent();

		getAllComponents(this).forEach(item -> {
			item.addKeyListener(new KeyAdapter() {
				private boolean isCtrlPressed = false;

				@Override
				public void keyPressed(KeyEvent e) {
					if(isCtrlPressed) {
						//Nh???n ph??m N khi ??ang gi??? ph??m Ctrl
						if(e.getKeyCode() == KeyEvent.VK_N)
							btnLapHoaDon.doClick();
						else if(e.getKeyCode() == KeyEvent.VK_LEFT)
							btnQuayLai.doClick();
						else if(e.getKeyCode() == KeyEvent.VK_E)
							btnXoaTrang.doClick();
						else if(e.getKeyCode() == KeyEvent.VK_F)
							btnTimKiem.doClick();

					} else if(e.getKeyCode() == KeyEvent.VK_CONTROL)
						isCtrlPressed = true;
					else 
						isCtrlPressed = false;
				}

				@Override
				public void keyReleased(KeyEvent e) {
					isCtrlPressed = false;
				}
			});
		});

		hoaDonDAO = new HoaDonDAO();
		taiDuLieuLenBang(hoaDonDAO.lay20HoaDonGanDay(), 0);
	}

	private void taiDuLieuLenBang(List<HoaDon> dsHD, int minIndex) {
		if(minIndex >= dsHD.size() || minIndex < 0)
			return;
		
		lbPage.setText("Trang " + (minIndex / 20 + 1) + " trong " + ((dsHD.size() - 1) / 20 + 1) + " trang.");
		
		modelHD.setRowCount(0);
		hoaDons = dsHD;
		modelHD.getDataVector().removeAllElements();
		modelHD.fireTableDataChanged();
		NumberFormat nf = NumberFormat.getInstance(Locale.US);
		nf.setMinimumIntegerDigits(2);
		nf.setMaximumFractionDigits(2);

		for(int i = minIndex; i < minIndex + 20; i++) {
			if(i >= dsHD.size())
				break;
			HoaDon hd = dsHD.get(i);
			SwingUtilities.invokeLater(() -> {
				modelHD.addRow(new Object[] {
						hd.getMaHoaDon(), hd.getKhachHang() == null ? null : hd.getKhachHang().getTenKhachHang(),
								hd.getNhanVienLap() == null ? null : hd.getNhanVienLap().getTenNhanVien(), hd.getNgayLap().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
										nf.format(hoaDonDAO.tinhTongTien(hd))
				});
			});
		}
		
		currentIndex = minIndex;

	}

	public Component getDefaultFocusComponent() {
		return txtMaHoaDon;
	}

	public List<Component> getAllComponents(Container c) {
		Component[] comps = c.getComponents();
		List<Component> compList = new ArrayList<Component>();
		for (Component comp : comps) {
			compList.add(comp);
			if (comp instanceof Container)
				compList.addAll(getAllComponents((Container) comp));
		}
		return compList;
	}
	
	private void timHoaDon() {
		new Thread(() -> {
			LocalDate d = null;
			if(dateNgayLap.getDate() != null)
				d = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(dateNgayLap.getDate()) );

			List<HoaDon> dsKQ = hoaDonDAO.timHoaDon(txtMaHoaDon.getText(), txtTenKhachHang.getText(), 
					txtTenNhanVien.getText(), d);

			if(dsKQ.size() > 0) {
				taiDuLieuLenBang(dsKQ, 0);
			}
			else {
				hoaDons.clear();
				modelHD.setRowCount(0);
			}
		}).start();
	}

	private void addEvent() {
		txtMaHoaDon.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timHoaDon();
			}
		});
		txtTenKhachHang.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timHoaDon();
			}
		});
		txtTenNhanVien.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timHoaDon();
			}
		});
		
		btnHome.addActionListener(e -> {
			if(hoaDons != null) {
				taiDuLieuLenBang(hoaDons, 0);
			}
		});

		btnEnd.addActionListener(e -> {
			if(hoaDons != null) {
				taiDuLieuLenBang(hoaDons, hoaDons.size() - hoaDons.size() % 20);
			}
		});

		btnBefore.addActionListener(e -> {
			if(hoaDons != null) {
				taiDuLieuLenBang(hoaDons, currentIndex  - 20);
			}
		});

		btnNext.addActionListener(e -> {
			if(hoaDons != null) {
				taiDuLieuLenBang(hoaDons, currentIndex + 20);
			}
		});

		btnXemHD.addActionListener((e) -> {
			int row = tableHoaDon.getSelectedRow();
			if(row == -1) {
				UIConstant.showInfo(QuanLyHoaDonPanel.this, "Ch??a ch???n h??a ????n c???n xem!!");
				return;
			}
			
			row += currentIndex;

			new CapNhatHoaDonDialog(this).setHoaDon(hoaDons.get(row));
		});
		btnLapHoaDon.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new CapNhatHoaDonDialog(QuanLyHoaDonPanel.this).setVisible(true);
			}
		});

		btnTimKiem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				timHoaDon();

			}
		});

		btnXoaTrang.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(tableHoaDon.getSelectedRow() != -1)
					tableHoaDon.clearSelection();

				txtMaHoaDon.setText("");
				txtTenKhachHang.setText("");
				txtTenNhanVien.setText("");
				dateNgayLap.setDate(null);

				txtMaHoaDon.requestFocus();
			}
		});

		tableHoaDon.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(tableHoaDon.getSelectedRow() == -1)
					return;

				int row = tableHoaDon.getSelectedRow() + currentIndex;

				txtMaHoaDon.setText(hoaDons.get(row).getMaHoaDon());
				txtTenKhachHang.setText(hoaDons.get(row).getKhachHang() == null ? "" : hoaDons.get(row).getKhachHang().getTenKhachHang());
				txtTenNhanVien.setText(hoaDons.get(row).getNhanVienLap() == null ? "" : hoaDons.get(row).getNhanVienLap().getTenNhanVien());
				try {
					Date date = new SimpleDateFormat("yyyy-MM-dd").parse(hoaDons.get(row).getNgayLap().toString());

					dateNgayLap.setDate(date);
				} catch (ParseException e1) {

				}

			}
		});

		btnQuayLai.addActionListener((e) -> {
			mainFrame.changeCenter(mainFrame.getTrangChuPanel());
		});
	}

	private void setLookAndFeel() {
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
					break;

				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			
		}
	}


}

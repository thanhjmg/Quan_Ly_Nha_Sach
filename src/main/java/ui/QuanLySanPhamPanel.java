package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.sql.rowset.serial.SerialBlob;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

//import dao.KhachHangDAO;
import dao.SanPhamDao;
//import dao.ThuocDAO;
//import entity.KhachHang;
import entity.NhaCungCap;
//import entity.PhanLoai;
import entity.SanPham;
//import entity.Thuoc;
import net.java.balloontip.BalloonTip;

public class QuanLySanPhamPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ColoredButton btnSearch;
	private DefaultTableModel model;
	private CustomTable table;
	private ColoredButton btnThem;
	private ColoredButton btnSua;
	private ColoredButton btnXoa;
	private ColoredButton btnQuayLai;
	private List<SanPham> dsKetQua;
	private ColoredButton btnChonHinhAnh;
	private ColoredButton btnChonNhaCungCap;
	private NhaCungCap nhaCungCap;
	private ChonNhaCungCapDialog chonNhaCungCapDialog;
	private SanPhamDao sanPhamDAO;
	private JTextField txtMa;
    
	private final Dimension dimension = new Dimension(100, 25);
	private JTextField txtTen;
	private JTextField txtTacGia;
	private JComboBox<String> cbbGT;
	private JTextField txtNcc;
	private JTextField txtTheLoai;
	private JComboBox<String> cbbPL;
	private JTextField txtSoLuong;
	//private JTextField txtGiaThanh;
	private JTextField txtHinhAnh;
	private DefaultTableModel modelTimKiem;
	private JLabel lblPage;
	private ColoredButton btnXoaTrang;
    private ColoredButton btnTrangThai;
//	private KhachHangDAO khachHangDAO;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JTextField txtTen2;
	private JTextField txtNcc2;
	private JTextField txtTacGia2;
	private JTextField txtTheLoai2;
    private JTextField txtDiaChi;
    private JTextField txtGia;
	private ColoredButton btnXoaTrang2;
	private JTabbedPane tabbedPane;
	private MainFrame mainFrame;
	private DefaultTableModel defaultTable;
	private List<SanPham> sp = null;
	private ColoredButton btnHome;
	private ColoredButton btnEnd;
	private ColoredButton btnBefore;
	private ColoredButton btnNext;
	private int currentIndex = 0;
    
//	private BalloonTip ballTen2;
//	private BalloonTip ballNcc2;
//	private BalloonTip ballTacGia2;
//	private BalloonTip ballTheLoai2;
//	
	private BalloonTip ballTen;
	private BalloonTip ballTacGia;
	private BalloonTip ballNcc;
	private BalloonTip ballDonGia;
	private BalloonTip ballPhanLoai;
	private BalloonTip ballSoLuong;
	private BalloonTip ballTheLoai;
	private SanPham selectedSanPham;

	public QuanLySanPhamPanel(MainFrame mainFrame) {
		
		setOpaque(true);
		setLayout(new BorderLayout());
		setBackground(Color.white);
		setLookAndFeel();

     	addControls();
     	addEvents();
     	
		

		sanPhamDAO = new SanPhamDao();
		getAllComponents(this).forEach(item -> {
			item.addKeyListener(new KeyAdapter() {
				private boolean isCtrlPressed = false;

				@Override
				public void keyPressed(KeyEvent e) {
					if(isCtrlPressed) {
						//Nh???n ph??m N khi ??ang gi??? ph??m Ctrl
						if(e.getKeyCode() == KeyEvent.VK_N)
							btnThem.doClick();
						else if(e.getKeyCode() == KeyEvent.VK_X)
							btnXoa.doClick();
						else if(e.getKeyCode() == KeyEvent.VK_LEFT)
							btnQuayLai.doClick();
						else if(e.getKeyCode() == KeyEvent.VK_E) {
							btnXoaTrang.doClick();
							btnXoaTrang2.doClick();
						}

						else if(e.getKeyCode() == KeyEvent.VK_F)
							btnSearch.doClick();

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

		dsKetQua = new ArrayList<SanPham>();

		loadData();
	}
	public Component getDefaultFocusComponent() {
		return txtTen2;
	}
	

//	private void taiDuLieuLenBang(List<KhachHang> dsKH, int minIndex) {
//		if(minIndex >= dsKH.size() || minIndex < 0)
//			return;
//
//		lbPage.setText("Trang " + (minIndex / 20 + 1) + " trong " + ((dsKH.size() - 1) / 20 + 1) + " trang.");
//
//		model.setRowCount(0);
//		model.getDataVector().removeAllElements();
//		model.fireTableDataChanged();
//		NumberFormat nf = NumberFormat.getInstance(Locale.US);
//		nf.setMinimumIntegerDigits(2);
//		nf.setMaximumFractionDigits(2);
//
//		for(int i = minIndex; i < minIndex + 20; i++) {
//			if(i >= dsKH.size())
//				break;
//			KhachHang khachHang = dsKH.get(i);
//			SwingUtilities.invokeLater(() -> {
//				LocalDate ns = khachHang.getNgaySinh();
//				String ngaySinh = null;
//				if(ns != null) 
//					ngaySinh = ns.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
//				
//				model.addRow(new Object[] { khachHang.getMaKhachHang(), khachHang.getTenKhachHang(), ngaySinh,
//						khachHang.getDiaChi(), khachHang.isGioiTinh() ? "Nam" : "N???",
//								khachHang.getEmail() == null ? "" : khachHang.getEmail(), khachHang.getSoDienThoai() });
//			});
//		}
//
//		currentIndex = minIndex;
//
//	}

//	private void loadData() {
//		dsKetQua = thuocDAO.lay20ThuocGanDay();
//
//		taiDuLieuLenBang(dsKetQua, 0);

//		for(String item : thuocDAO.getDSDangBaoChe()) {
//			dangBaoCheModel.addElement(item);
//			dangBCTKModel.addElement(item);
//		}
//
//		for(String item : thuocDAO.getDSDonViTinh()) {
//			donViTinhModel.addElement(item);
//			donViTinhTKModel.addElement(item);
//		}
//
//		for(String item : thuocDAO.getDSNuocSanXuat()) {
//			nuocSXModel.addElement(item);
//			nuocSXTKModel.addElement(item);
//		}

	//}

//	private void timKhachHang() {
//		String ten = txtTen2.getText().trim();
//
//		String diachi = txtTacGia2.getText().trim();
//		String email = txtTheLoai2.getText().trim();
//
//		String sdt = txtNcc2.getText().trim();
//
//		dsKH = khachHangDAO.timKhachHang(ten, diachi, email, sdt);
//		if (dsKH.size() != 0) {
//			taiDuLieuLenBang(dsKH, 0);
//
//			txtTen2.requestFocus();
//		} else {
//			txtTen2.requestFocus();
//			UIConstant.showInfo(QuanLySanPhamPanel.this, "Kh??ng t??m th???y kh??ch h??ng n??o !");
//		}
//	}
	

	

	private boolean validData() {
		String tenSanPham = txtTen.getText().trim();
		String tacGia = txtTacGia.getText().trim();
		String soLuong= txtSoLuong.getText().trim();
		String giaThanh= txtGia.getText().trim();
		String nhaCungCap= txtNcc.getText().trim();
	   
		Blob hinhAnh = null;
		String theLoai = txtTheLoai.getText().trim();
		
		// t??n
		if (tenSanPham.isEmpty()) {
			ballTen.setTextContents("T??n s???n ph???m kh??ng ???????c r???ng");
			ballTen.setVisible(true);
			return false;
		} 
		else if (!tenSanPham.matches("[\\p{Lu}[A-Z0-9]][\\p{L}[a-z0-9]]*(\\s+[\\p{Lu}[A-Z0-9]][\\p{L}[a-z0-9]]*)*")) {
			ballTen.setTextContents(" +  T??n s???n ph???m ph???i b???t ?????u ch??? c??i in hoa ho???c s??? \n"
					+ " + Kh??ng ch???a c??c k?? t??? ?????c bi???t");
			ballTen.setVisible(true);
			return false;
		} else 
			ballTen.setVisible(false);
		
		if (tacGia.isEmpty()) {
			ballTacGia.setTextContents("T??c gi??? kh??ng ???????c r???ng");
			ballTacGia.setVisible(true);
			return false;
		} 
		else if (!tacGia.matches("[\\p{Lu}[A-Z]][\\p{L}[a-z]]*(\\s+[\\p{Lu}[A-Z]][\\p{L}[a-z]]*)*")) {
			ballTacGia.setTextContents(" +  T??n t??c gi??? ph???i b???t ?????u ch??? c??i in hoa \n"
					+ " + Kh??ng ch???a c??c k?? t??? ?????c bi???t v?? s???");
			ballTacGia.setVisible(true);
			return false;
		} else 
			ballTacGia.setVisible(false);
	///	
		if (theLoai.isEmpty()) {
			ballTheLoai.setTextContents("Th??? lo???i kh??ng ???????c r???ng");
			ballTheLoai.setVisible(true);
			return false;
		} 
		else if (!theLoai.matches("[\\p{Lu}[A-Z]][\\p{L}[a-z]]*(\\s+[\\p{Lu}[A-Z]][\\p{L}[a-z]]*)*")) {
			ballTheLoai.setTextContents(" +  Th??? lo???i ph???i b???t ?????u ch??? c??i in hoa \n"
					+ " + Kh??ng ch???a c??c k?? t??? ?????c bi???t v?? s???");
			ballTheLoai.setVisible(true);
			return false;
		} else 
			ballTheLoai.setVisible(false);
	/////
		
		if (giaThanh.isEmpty()) {
			ballDonGia.setTextContents("Gi?? th??nh kh??ng ???????c r???ng");
			ballDonGia.setVisible(true);
			return false;
		}
		double gia= Double.parseDouble(giaThanh);
		if(gia<0) {
			ballDonGia.setTextContents("Gi?? th??nh kh??ng ???????c ??m");
			ballDonGia.setVisible(true);
			return false;
		}
		 else 
			ballDonGia.setVisible(false);
	////
		
		if (soLuong.isEmpty()) {
			ballSoLuong.setTextContents("S??? l?????ng kh??ng ???????c r???ng");
			ballSoLuong.setVisible(true);
			return false;
		}
		int sl= Integer.parseInt(soLuong);
		if(sl < 0) {
			ballSoLuong.setTextContents("S??? l?????ng kh??ng ???????c ??m");
			ballSoLuong.setVisible(true);
			return false;
		}
		 else 
			ballSoLuong.setVisible(false);
///
		if (nhaCungCap.isEmpty()) {
			ballNcc.setTextContents("Vui l??ng ch???n nh?? cung c???p");
			ballNcc.setVisible(true);
			return false;
		} 
		
//		

//		// s??? ??i???n tho???i
//		if (!sdt.matches("[0]\\d{9}")) {
//			ballSDT.setTextContents("S??? ??i???n tho???i g???m 10 ch??? s???, b???t ?????u l?? 0");
//			ballSDT.setVisible(true);
//			return false;
//		}
//		ballSDT.setVisible(false);
//
//		// ??ia ch???
//		if (diaChi.isEmpty()) {
//			ballDC.setTextContents("?????a ch??? kh??ng ???????c r???ng");
//			ballDC.setVisible(true);
//			return false;
//		}
//		ballDC.setVisible(false);
//
//		// email
//		if(!email.isEmpty()) {
//			if (!email.matches("^[a-z][a-z0-9\\.]{7,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$")) {
//				ballEmail.setTextContents(
//						" + Email ph???i b???t ?????u b???ng 1 k?? t??? \n" + "+ Ch??? ch???a k?? t??? a-z, 0-9 v?? k?? t??? d???u ch???m (.) \n"
//								+ "+ ????? d??i t???i thi???u l?? 8, ????? d??i t???i ??a l?? 32 \n"
//								+ " +T??n mi???n c?? th??? l?? t??n mi???n c???p 1 ho???c c???p 2");
//				ballEmail.setVisible(true);
//				return false;
//			}
//		}
//		ballEmail.setVisible(false);


		return true;
	}

	private void addControls() {
		
        
		jPanel1 = new JPanel(new BorderLayout());
		jPanel1.setOpaque(false);
		jPanel2 = new JPanel(new BorderLayout());
		jPanel2.setOpaque(false);
	
		JPanel pnlTitle = new JPanel();
		pnlTitle.setOpaque(false);
		JLabel lblHeader = new JLabel("Qu???n L?? S???n Ph???m");
		lblHeader.setFont(new Font("Arial", Font.BOLD, 20));
		lblHeader.setHorizontalAlignment(JLabel.CENTER);
		lblHeader.setForeground(UIConstant.PRIMARY_COLOR);
		pnlTitle.add(lblHeader);

		Box boxNorth = Box.createVerticalBox();
		boxNorth.add(pnlTitle);

		addNorth();
		addCenter();
		addSouth();
		addNorth2();
		
        
		
		
		tabbedPane = new JTabbedPane();

		tabbedPane.addTab("T??m ki???m", jPanel2);
		tabbedPane.setBackground(UIConstant.BELOW_COLOR);
		tabbedPane.addTab("C???p nh???t s???n ph???m", jPanel1);

		tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));
		
		boxNorth.add(tabbedPane);
		this.add(boxNorth, BorderLayout.NORTH);

	}

	private void addSouth() {
		Box boxSouth = Box.createVerticalBox();
		boxSouth.add(Box.createVerticalStrut(20));
		JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));



		btnQuayLai = new ColoredButton("Quay l???i", new ImageIcon("Images//back.png"));
		btnQuayLai.setFont(UIConstant.NORMAL_FONT);
		btnQuayLai.setBorderRadius(30);
		btnQuayLai.setBackground(UIConstant.DANGER_COLOR);
		
		pnlSouth.add(btnQuayLai);
		pnlSouth.setOpaque(false);
		boxSouth.add(pnlSouth);
		this.add(boxSouth, BorderLayout.SOUTH);
	}
	

	private void addCenter() {

		Box boxCenter = Box.createVerticalBox();

		JPanel pnlDanhSach = new JPanel(new BorderLayout());
	

		String[] columns = new String[] { "H??nh ???nh", "M?? s???n ph???m", "T??n s???n ph???m", "T??c gi???", "Nh?? cung c???p", "????n gi??", "Ph??n lo???i","S??? l?????ng","Th??? lo???i"};
        
		
		
		model = new DefaultTableModel(columns, 0);
		table = new CustomTable(model);
        
		table.setAutoCreateRowSorter(true);

		JScrollPane pane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pane.getViewport().setBackground(Color.WHITE);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Danh s??ch k???t qu???", pane);

		lblPage = new JLabel("Trang 1 trong 1 trang."); lblPage.setFont(UIConstant.NORMAL_FONT);

		btnHome = new ColoredButton(new ImageIcon("Images/double_left.png")); btnHome.setBackground(UIConstant.LINE_COLOR);
		btnEnd = new ColoredButton(new ImageIcon("Images/double_right.png")); btnEnd.setBackground(UIConstant.LINE_COLOR);
		btnBefore = new ColoredButton(new ImageIcon("Images/left.png")); btnBefore.setBackground(UIConstant.LINE_COLOR);
		btnNext = new ColoredButton(new ImageIcon("Images/right.png")); btnNext.setBackground(UIConstant.LINE_COLOR);

		btnHome.setToolTipText("Trang ?????u");
		btnEnd.setToolTipText("Trang cu???i");
		btnBefore.setToolTipText("Trang tr?????c");
		btnNext.setToolTipText("Trang k??? ti???p");

		Box boxPage = Box.createHorizontalBox();
		boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnHome); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnBefore); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnNext); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnEnd); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(Box.createHorizontalGlue());
		boxPage.add(lblPage); boxPage.add(Box.createHorizontalStrut(5));

		boxCenter.add(tabbedPane);
		boxCenter.add(Box.createVerticalStrut(5));
		boxCenter.add(boxPage);
		pnlDanhSach.add(boxCenter, BorderLayout.CENTER);
		pnlDanhSach.setOpaque(false);

		this.add(pnlDanhSach, BorderLayout.CENTER);
	}

	private void addNorth2() {
		
		
		
		JPanel pnlCenter = new JPanel(new BorderLayout());
		Box boxMain = Box.createVerticalBox();
		Box boxLine2 = Box.createHorizontalBox();

		JLabel lblTen2 = new JLabel("T??n s???n ph???m:");
		lblTen2.setFont(UIConstant.NORMAL_FONT);
		txtTen2 = new JTextField(1);
		txtTen2.setFont(UIConstant.NORMAL_FONT);
		lblTen2.setPreferredSize(dimension);

		JLabel lblNcc2 = new JLabel("Nh?? cung c???p:");
		lblNcc2.setFont(UIConstant.NORMAL_FONT);
		txtNcc2 = new JTextField(1);
		txtNcc2.setFont(UIConstant.NORMAL_FONT);
		lblNcc2.setPreferredSize(dimension);

		boxLine2.add(lblTen2);
		boxLine2.add(txtTen2);
		boxLine2.add(Box.createHorizontalStrut(20));
		boxLine2.add(lblNcc2);
		boxLine2.add(txtNcc2);

		// line 3
		Box boxLine3 = Box.createHorizontalBox();

		JLabel lblTacGia2 = new JLabel("T??c gi???:");
		lblTacGia2.setFont(UIConstant.NORMAL_FONT);
		txtTacGia2 = new JTextField(1);
		txtTacGia2.setFont(UIConstant.NORMAL_FONT);
		lblTacGia2.setPreferredSize(lblTen2.getMaximumSize());
		lblTacGia2.setPreferredSize(dimension);

		JLabel lblTheLoai2 = new JLabel("Th??? lo???i:");
		txtTheLoai2 = new JTextField(1);
		lblTheLoai2.setFont(UIConstant.NORMAL_FONT);
		txtTheLoai2.setFont(UIConstant.NORMAL_FONT);
		lblTheLoai2.setPreferredSize(lblNcc2.getMaximumSize());
		lblTheLoai2.setPreferredSize(dimension);

		boxLine3.add(lblTacGia2);
		boxLine3.add(txtTacGia2);
		boxLine3.add(Box.createHorizontalStrut(20));
		boxLine3.add(lblTheLoai2);
		boxLine3.add(txtTheLoai2);
		// buton

		JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 30));
		btnSearch = new ColoredButton("T??m ki???m", new ImageIcon("Images//search.png"));
		btnSearch.setFont(UIConstant.NORMAL_FONT);
		btnSearch.setBorderRadius(30);
		btnSearch.setBackground(UIConstant.DANGER_COLOR);
	  
		btnXoaTrang2 = new ColoredButton("X??a r???ng", new ImageIcon("Images//empty.png"));
		btnXoaTrang2.setBackground(UIConstant.DANGER_COLOR);
		btnXoaTrang2.setFont(UIConstant.NORMAL_FONT);
		btnXoaTrang2.setBorderRadius(30);
		
		
		
		

		pnlButton.add(btnSearch);
	
	

    
		pnlButton.add(btnXoaTrang2);
		pnlButton.setOpaque(false);

		
		boxMain.add(Box.createVerticalStrut(20));
		boxMain.add(boxLine2);
		boxMain.add(Box.createVerticalStrut(20));
		boxMain.add(boxLine3);
		
	
		pnlCenter.add(boxMain, BorderLayout.CENTER);
		pnlCenter.add(pnlButton, BorderLayout.SOUTH);
		pnlCenter.setOpaque(false);
		
		
		
		
		jPanel2.add(pnlCenter, BorderLayout.NORTH);

	}

	/// C???p nh???t kh??ch h??ng
	private void addNorth() {
		
		
		
		
		JPanel pnlCenter = new JPanel(new BorderLayout());
		Box boxCapnhat = Box.createVerticalBox();
		Box box2 = Box.createHorizontalBox();

		JLabel lblMaSP = new JLabel("M?? s???n ph???m:");
		lblMaSP.setFont(UIConstant.NORMAL_FONT);
		txtMa = new JTextField(1);
		txtMa.setFont(UIConstant.NORMAL_FONT);
		txtMa.setEnabled(false);
		lblMaSP.setPreferredSize(dimension);

		JLabel lblTenSP = new JLabel("T??n s???n ph???m:");
		lblTenSP.setFont(UIConstant.NORMAL_FONT);
		txtTen = new JTextField(1);
		txtTen.setFont(UIConstant.NORMAL_FONT);
		lblTenSP.setPreferredSize(dimension);

		box2.add(lblMaSP);
		box2.add(txtMa);
		box2.add(Box.createHorizontalStrut(20));
		box2.add(lblTenSP);
		box2.add(txtTen);

		// line 3
		Box box3 = Box.createHorizontalBox();

		JLabel lblTacGia = new JLabel("T??c gi???:");
		lblTacGia.setFont(UIConstant.NORMAL_FONT);
		txtTacGia = new JTextField(1);
		txtTacGia.setFont(UIConstant.NORMAL_FONT);
		lblTacGia.setPreferredSize(lblMaSP.getMaximumSize());
		lblTacGia.setPreferredSize(dimension);

		JLabel lblTheLoai = new JLabel("Th??? lo???i:");
		txtTheLoai = new JTextField(1);
		lblTheLoai.setFont(UIConstant.NORMAL_FONT);
		txtTheLoai.setFont(UIConstant.NORMAL_FONT);
		lblTheLoai.setPreferredSize(lblTenSP.getMaximumSize());
		lblTheLoai.setPreferredSize(dimension);
		

		box3.add(lblTacGia);
		box3.add(txtTacGia);
		box3.add(Box.createHorizontalStrut(20));
		box3.add(lblTheLoai);
		box3.add(txtTheLoai);
		
		//line 4
				Box box4 = Box.createHorizontalBox();

				JLabel lblPhanLoai = new JLabel("Ph??n Lo???i:");
				lblPhanLoai.setFont(UIConstant.NORMAL_FONT);
				lblPhanLoai.setPreferredSize(lblMaSP.getMaximumSize());
				lblPhanLoai.setPreferredSize(dimension);
				cbbPL = new JComboBox<String>();
				cbbPL.setFont(UIConstant.NORMAL_FONT);
				cbbPL.addItem("D???ng c??? h???c t???p");
				cbbPL.addItem("S??ch");
				JLabel lblGia = new JLabel("Gi?? th??nh:");
				txtGia = new JTextField(1);
				lblGia.setFont(UIConstant.NORMAL_FONT);
				txtGia.setFont(UIConstant.NORMAL_FONT);
				lblGia.setPreferredSize(lblTenSP.getMaximumSize());
				lblGia.setPreferredSize(dimension);
				
				JLabel lblSoLuong = new JLabel("S??? l?????ng:");
				txtSoLuong = new JTextField(1);
				lblSoLuong.setFont(UIConstant.NORMAL_FONT);
				txtSoLuong.setFont(UIConstant.NORMAL_FONT);
				lblSoLuong.setPreferredSize(lblTenSP.getMaximumSize());
				lblSoLuong.setPreferredSize(dimension);
				
				box4.add(lblPhanLoai);
				box4.add(cbbPL);
				box4.add(Box.createHorizontalStrut(20));
				box4.add(lblGia);
				box4.add(txtGia);
				box4.add(Box.createHorizontalStrut(10));
				box4.add(lblSoLuong);
				box4.add(txtSoLuong);
				
				
				// line 5
				Box box5 = Box.createHorizontalBox();
				JLabel lblNCC = new JLabel("Nh?? cung c???p:");
				txtNcc = new JTextField(1);
				lblNCC.setFont(UIConstant.NORMAL_FONT);
				txtNcc.setFont(UIConstant.NORMAL_FONT);
				txtNcc.setEnabled(false);
				lblNCC.setPreferredSize(lblTenSP.getMaximumSize());
				lblNCC.setPreferredSize(lblMaSP.getMaximumSize());
				lblNCC.setPreferredSize(dimension);
				btnChonNhaCungCap = new ColoredButton("Ch???n nh?? cung c???p"); btnChonNhaCungCap.setBackground(UIConstant.PRIMARY_COLOR);
				
				box5.add(lblNCC);
				box5.add(Box.createHorizontalStrut(0));
				box5.add(txtNcc); 
				box5.add(Box.createHorizontalStrut(5));
				box5.add(btnChonNhaCungCap); 
				box5.add(Box.createHorizontalStrut(5));
				// line 6
				Box box6 = Box.createHorizontalBox();
				JLabel lblHinhAnh = new JLabel("H??nh ???nh:");
				txtHinhAnh = new JTextField(1);
				lblHinhAnh.setFont(UIConstant.NORMAL_FONT);
				txtHinhAnh.setFont(UIConstant.NORMAL_FONT);
				txtHinhAnh.setEnabled(false);
				lblHinhAnh.setPreferredSize(lblTenSP.getMaximumSize());
				
				lblHinhAnh.setPreferredSize(dimension);
				btnChonHinhAnh = new ColoredButton("Ch???n h??nh ???nh"); btnChonHinhAnh.setBackground(UIConstant.PRIMARY_COLOR);
				
				box6.add(lblHinhAnh);
				box6.add(Box.createHorizontalStrut(0));
				box6.add(txtHinhAnh); 
				box6.add(Box.createHorizontalStrut(32));
				box6.add(btnChonHinhAnh); 
				box6.add(Box.createHorizontalStrut(5));

		
		// buton

		JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));

		btnThem = new ColoredButton("Th??m s???n ph???m", new ImageIcon("Images/add.png"));
		btnThem.setFont(UIConstant.NORMAL_FONT);
		btnThem.setBorderRadius(30);
		btnThem.setBackground(UIConstant.DANGER_COLOR);
		
		btnSua = new ColoredButton("S???a th??ng tin", new ImageIcon("Images/modify.png"));
		btnSua.setFont(UIConstant.NORMAL_FONT);
		btnSua.setBorderRadius(30);
		btnSua.setBackground(UIConstant.DANGER_COLOR);
	  
		btnXoaTrang = new ColoredButton("X??a r???ng", new ImageIcon("Images//empty.png"));
		btnXoaTrang.setBackground(UIConstant.DANGER_COLOR);
		btnXoaTrang.setFont(UIConstant.NORMAL_FONT);
		btnXoaTrang.setBorderRadius(30);
		
		btnXoa = new ColoredButton("X??a s???n ph???m", new ImageIcon("Images//delete.png"));
		btnXoa.setBackground(UIConstant.DANGER_COLOR);
		btnXoa.setFont(UIConstant.NORMAL_FONT);
		btnXoa.setBorderRadius(30);
		pnlButton.add(btnThem);
	    pnlButton.add(btnSua);
        pnlButton.add(btnXoa);
    
		pnlButton.add(btnXoaTrang);
		pnlButton.setOpaque(false);

	
		ballTen = new BalloonTip(txtTen, "T??n s???n ph???m kh??ng ???????c r???ng!"); ballTen.setVisible(false); ballTen.setCloseButton(null);
		ballTacGia = new BalloonTip(txtTacGia, "T??n t??c gi??? kh??ng ???????c r???ng!"); ballTacGia.setVisible(false); ballTacGia.setCloseButton(null);
		ballTheLoai = new BalloonTip(txtTheLoai, "Th??? lo???i kh??ng ???????c r???ng!"); ballTheLoai.setVisible(false); ballTheLoai.setCloseButton(null);
		ballDonGia = new BalloonTip(txtGia, "Gi?? th??nh kh??ng ???????c r???ng, ch??? nh???p s??? v?? l???n h??n !"); ballDonGia.setVisible(false); ballDonGia.setCloseButton(null);
		ballSoLuong = new BalloonTip(txtSoLuong, "S??? l?????ng kh??ng ???????c r???ng!"); ballSoLuong.setVisible(false); ballSoLuong.setCloseButton(null);
		ballNcc = new BalloonTip(txtNcc, "Vui l??ng ch???n nh?? cung c???p!"); ballNcc.setVisible(false); ballNcc.setCloseButton(null);
		
		
		boxCapnhat.add(Box.createVerticalStrut(5));
		boxCapnhat.add(box2);
		boxCapnhat.add(Box.createVerticalStrut(5));
		boxCapnhat.add(box3);
		boxCapnhat.add(Box.createVerticalStrut(5));
		boxCapnhat.add(box4);
		boxCapnhat.add(Box.createVerticalStrut(5));
		boxCapnhat.add(box5);
		boxCapnhat.add(Box.createVerticalStrut(5));
		boxCapnhat.add(box6);
		
	
		pnlCenter.add(boxCapnhat, BorderLayout.CENTER);
		pnlCenter.add(pnlButton, BorderLayout.SOUTH);
		pnlCenter.setOpaque(false);
		
		
		
		
		
		jPanel1.add(pnlCenter, BorderLayout.NORTH);

	}
	public void setNhaCungCapDaChon(NhaCungCap nhaCungCap) 
	{
		this.nhaCungCap = nhaCungCap;
		txtNcc.setText(nhaCungCap.getTenNCC());
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
	public void themKhachHangMoi() {
		tabbedPane.setSelectedIndex(1);
		this.setVisible(true);
	}
	
	private void addEvents() {
		this.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().equals("ancestor")) {
					tabbedPane.setSelectedIndex(0);
				}
			}
		});
//		
		txtTen2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timSanPham();
			}
		});
		txtNcc2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timSanPham();
			}
		});
		txtTacGia2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timSanPham();
			}
		});
		txtTheLoai2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timSanPham();
			}
		});
	

		
		
		btnXoaTrang2.addActionListener(new ActionListener() {
			
						@Override
						public void actionPerformed(ActionEvent e) {
			
							txtTen2.setText("");
							txtTen2.requestFocus();
							txtNcc2.setText("");
							txtTacGia2.setText("");
							txtTheLoai2.setText("");
						}
			});
		btnXoaTrang.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				txtMa.setText("T??? ?????ng t???o");
				txtTen.setText("");
				txtTacGia.setText("");
				txtTheLoai.setText("");
				cbbPL.setSelectedIndex(0);
				txtGia.setText("");
				txtSoLuong.setText("");
				txtHinhAnh.setText("");
				txtTen.requestFocus();
				nhaCungCap = null;

				ballTen.setVisible(false);
				ballTacGia.setVisible(false);
				ballNcc.setVisible(false);
				ballDonGia.setVisible(false);
				ballSoLuong.setVisible(false);
				ballTheLoai.setVisible(false);
				
			}
		});

		
table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int row = table.getSelectedRow();
				if(row == -1)
					return;
				txtHinhAnh.setText(table.getValueAt(row, 0).toString());
				txtMa.setText(table.getValueAt(row, 1).toString());
				txtTen.setText(table.getValueAt(row, 2).toString());
				txtTacGia.setText(table.getValueAt(row, 3).toString());
				txtNcc.setText(table.getValueAt(row, 4).toString());
				txtGia.setText(table.getValueAt(row, 5).toString());
				cbbPL.setSelectedItem(table.getValueAt(row, 6).toString().equals("D???ng c??? h???c t???p") ? "D???ng c??? h???c t???p" : "S??ch");
				txtSoLuong.setText(table.getValueAt(row, 7).toString());
				txtTheLoai.setText(table.getValueAt(row, 8).toString());

			}
		});
//		btnTrangThai.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				int row = table.getSelectedRow();
//				if(row == -1) {
//					UIConstant.showInfo(QuanLySanPhamPanel.this, "Ch??a ch???n s???n ph???m");
//				} else {
//					int click = JOptionPane.showConfirmDialog(QuanLySanPhamPanel.this, "B???n c?? ch???c ch??n mu???n chuy???n tr???ng th??i sang ng???ng b??n kh??ng?", "C???nh b??o", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("Images//warning.png"));
//					if(click == JOptionPane.YES_OPTION) {
//						if(sanPhamDAO.trangThai(model.getValueAt(table.getSelectedRow(), 1).toString().trim())) {
//							UIConstant.showInfo(QuanLySanPhamPanel.this, "???? chuy???n tr???ng th??i sang ng???ng b??n!");
//							int ind = dsKetQua.indexOf(selectedSanPham);
//							selectedSanPham.setTrangThai(false);
//							dsKetQua.set(ind, selectedSanPham);
//							model.setValueAt("Ng???ng b??n", table.getSelectedRow(), 10);
//
//						} else
//							UIConstant.showInfo(QuanLySanPhamPanel.this, "Chuy???n tr???ng th??i th???t b???i!");
//					}
//				}
//			}
//		});
		
btnXoa.addActionListener(new ActionListener() {

	@Override
	public void actionPerformed(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row == -1) {
			UIConstant.showInfo(QuanLySanPhamPanel.this, "B???n ch??a ch???n s???n ph???m ????? x??a");
			return;
		} else {
			int click = JOptionPane.showConfirmDialog(QuanLySanPhamPanel.this,
					"B???n c?? ch???c ch??n mu???n x??a kh??ng ?", "C???nh b??o", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, new ImageIcon("Images//warning.png"));
			if (click == JOptionPane.YES_OPTION) {
				if (sanPhamDAO.xoaSanPham(txtMa.getText().toString().trim())) {
					UIConstant.showInfo(QuanLySanPhamPanel.this, "X??a th??nh c??ng");
					model.removeRow(row);
					dsKetQua.remove(row + currentIndex);
//					xoaRong();
				} else
					UIConstant.showInfo(QuanLySanPhamPanel.this, "X??a th???t b???i");
			} else
				return;
		}
	}
});

	
//btnXoa.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				int row = table.getSelectedRow();
//				if(row == -1) {
//					UIConstant.showInfo(QuanLySanPhamPanel.this, "Ch??a ch???n nh?? cung c???p");
//					return;
//				} 
//				
//				if(JOptionPane.showConfirmDialog(QuanLySanPhamPanel.this, "B???n c?? ch???c l?? mu???n x??a!", "X??c nh???n", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION) {
//					return;
//				}
//				if(sanPhamDAO.deleteSanPham(txtNcc2.getText().toString())) {
//					UIConstant.showInfo(QuanLySanPhamPanel.this, "Xo?? th??nh c??ng");
//					
//					defaultTable.removeRow(row);
//					sp.remove(row + currentIndex);
//					XoaRong();
//						
//				} else
//					UIConstant.showWarning(QuanLySanPhamPanel.this, "Xo?? kh??ng th??nh c??ng");
//				
//			}
//		});
		
//		btnQuayLai.addActionListener((e) -> {
//			mainFrame.changeCenter(mainFrame.getTrangChuPanel());
//		});
//		
//		txtEmail2.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyReleased(KeyEvent e) {
//				timKhachHang();
//			}
//		});
//		
//		txtSdt2.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyReleased(KeyEvent e) {
//				timKhachHang();
//			}
//		});
		
		btnChonHinhAnh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setFileFilter(new FileNameExtensionFilter("Images file (*.png,*.jpg)", "png", "jpg"));
				int selection = fileChooser.showOpenDialog(QuanLySanPhamPanel.this);

				if(selection == JFileChooser.OPEN_DIALOG) {
					File selectedFile = fileChooser.getSelectedFile();
					if (!selectedFile.exists()) {
						UIConstant.showWarning(QuanLySanPhamPanel.this, "File kh??ng t???n t???i!");
						return ;
					} else {
						txtHinhAnh.setText(selectedFile.getAbsolutePath());
					}

				}
			}
		});
		btnChonNhaCungCap.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(chonNhaCungCapDialog == null)
					chonNhaCungCapDialog = new ChonNhaCungCapDialog(QuanLySanPhamPanel.this);

				chonNhaCungCapDialog.setVisible(true);
			}
		});
		
		btnSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				timSanPham();
			}
		});
		btnThem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(validData()) {
				String tenSanPham = txtTen.getText().trim();
				String tacGia = txtTacGia.getText().trim();
				String theLoai= txtTheLoai.getText().trim();
				String phanLoai= "";
				if(cbbPL.getSelectedIndex() != -1)
					phanLoai= cbbPL.getSelectedItem().toString();
				else
					phanLoai = cbbPL.getEditor().getItem().toString().trim();
				String giaThanh= txtGia.getText();
				double giathanh= Double.parseDouble(giaThanh);
				String soLuong= txtSoLuong.getText();
				 int sl = Integer.parseInt(soLuong);  
				Blob hinhAnh = null;
				if(txtHinhAnh.getText() != "") {
					try {
						FileInputStream inputStream = new FileInputStream(txtHinhAnh.getText());
						hinhAnh = new SerialBlob(Files.readAllBytes(new File(txtHinhAnh.getText().trim()).toPath()));
						inputStream.close();
					} catch (SQLException | IOException e1) {
						e1.printStackTrace();
					}
				}
				
				SanPham sanPham = new SanPham(tenSanPham, nhaCungCap, theLoai, phanLoai, sl, tacGia, giathanh, hinhAnh);
				if(sanPhamDAO.themSanPham(sanPham)) {
					UIConstant.showInfo(QuanLySanPhamPanel.this,"Th??m th??nh c??ng!");
					dsKetQua.add(sanPham);
					
			        }
				else
					UIConstant.showInfo(QuanLySanPhamPanel.this,"S???n ph???m ???? t???n t???i!");
			}
			}
			});
		btnSua.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(tabbedPane.getSelectedIndex() == 0)
				tabbedPane.setSelectedIndex(1);
			else {
				if(table.getSelectedRow() == -1) {
					UIConstant.showInfo(QuanLySanPhamPanel.this, "Ch??a ch???n s???n ph???m");
					
				} else {
					
					if(validData()) {
					String ma= txtMa.getText().trim();
					String tenSanPham = txtTen.getText().trim();
					String tacGia = txtTacGia.getText().trim();
					String theLoai= txtTheLoai.getText().trim();
					String phanLoai= "";
					if(cbbPL.getSelectedIndex() != -1)
						phanLoai= cbbPL.getSelectedItem().toString();
					else
						phanLoai = cbbPL.getEditor().getItem().toString().trim();
					String giaThanh= txtGia.getText();
					double giathanh= Double.parseDouble(giaThanh);
					String soLuong= txtSoLuong.getText();
					 int sl = Integer.parseInt(soLuong);  
					Blob hinhAnh = null;
					if(txtHinhAnh.getText() != "") {
						try {
							FileInputStream inputStream = new FileInputStream(txtHinhAnh.getText());
							hinhAnh = new SerialBlob(Files.readAllBytes(new File(txtHinhAnh.getText().trim()).toPath()));
							inputStream.close();
						} catch (SQLException | IOException e1) {
							e1.printStackTrace();
						}
					}
					
					SanPham sanPham = new SanPham(tenSanPham, nhaCungCap, theLoai, phanLoai, sl, tacGia, giathanh, hinhAnh);
					sanPham.setMaSanPham(ma);
					if(sanPhamDAO.capNhatSanPham(sanPham)) {
						
						
							UIConstant.showInfo(QuanLySanPhamPanel.this, "S???a th??nh c??ng");
							defaultTable.setValueAt(sanPham.getHinhAnh(), table.getSelectedRow(), 0);
							defaultTable.setValueAt(sanPham.getMaSanPham(), table.getSelectedRow(), 1);
							defaultTable.setValueAt(sanPham.getTenSanPham(), table.getSelectedRow(),2);
							defaultTable.setValueAt(sanPham.getTacGia(), table.getSelectedRow(), 3);
							defaultTable.setValueAt(sanPham.getNhaCungCap(), table.getSelectedRow(), 4);
							defaultTable.setValueAt(sanPham.getGiaThanh(), table.getSelectedRow(), 5);
							defaultTable.setValueAt(sanPham.getPhanLoai(), table.getSelectedRow(), 6);
							defaultTable.setValueAt(sanPham.getSoLuong(), table.getSelectedRow(), 7);
							defaultTable.setValueAt(sanPham.getTheLoai(), table.getSelectedRow(), 8);
							
							
							dsKetQua.set(table.getSelectedRow() + currentIndex, sanPham);
							UIConstant.showInfo(QuanLySanPhamPanel.this, "C???p nh???t th??nh c??ng");
						} else
							UIConstant.showWarning(QuanLySanPhamPanel.this, "S???a kh??ng th??nh c??ng");
					
				}
			}
		}
		}
		});
		
	}
		

	
	private void loadData() {
		dsKetQua = sanPhamDAO.lay20SanPhamGanDay();
		taiDuLieuLenBang(dsKetQua, 0);
//		for(String item : sanPhamDAO.getDSDangBaoChe()) {
//			dangBaoCheModel.addElement(item);
//			dangBCTKModel.addElement(item);
//		}
//
//		for(String item : thuocDAO.getDSDonViTinh()) {
//			donViTinhModel.addElement(item);
//			donViTinhTKModel.addElement(item);
//		}
//
//		for(String item : thuocDAO.getDSNuocSanXuat()) {
//			nuocSXModel.addElement(item);
//			nuocSXTKModel.addElement(item);
//		}

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
	private void timSanPham() {
		String tenSanPham = txtTen2.getText().trim();
		String nhaCungCap = txtNcc2.getText().trim();
		String tacGia = txtTacGia2.getText().trim();
		String theLoai= txtTheLoai2.getText().trim();
		dsKetQua = sanPhamDAO.timSanPham(tenSanPham, nhaCungCap, tacGia, theLoai);
		
		taiDuLieuLenBang(dsKetQua, 0);
	}
	
	
	private void taiDuLieuLenBang(List<SanPham> dsSanPham, int minIndex) {
		if(dsSanPham.size()==0) {
			model.setRowCount(0);
			lblPage.setText("Trang " + 1 + "trong" + 1 + " trang.");
			return;
		}
		if(minIndex >= dsSanPham.size() || minIndex <0)
			return;
		lblPage.setText("Trang " + (minIndex / 20 + 1) + " trong " + ((dsSanPham.size() - 1) / 20 + 1) + " trang.");

		model.setRowCount(0);
		model.getDataVector().removeAllElements();
		model.fireTableDataChanged();
		NumberFormat nf = NumberFormat.getInstance(Locale.US);
		nf.setMinimumIntegerDigits(2);
		nf.setMaximumFractionDigits(2);

		for(int i = minIndex; i < minIndex + 20; i++) {
			if(i >= dsSanPham.size())
				break;
			SanPham sanPham = dsSanPham.get(i);
			SwingUtilities.invokeLater(() -> {
				String ha = "";
				byte[] b = null;
				if(sanPham.getHinhAnh() == null)
					ha =  "Images/image.png";
				else {
					try {
						Blob blob = sanPham.getHinhAnh();

						b = blob.getBytes(1, (int) blob.length());

					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				} 
				model.addRow(new Object[] {ha != "" ? ha : b , sanPham.getMaSanPham(),sanPham.getTenSanPham(),sanPham.getTacGia() ,sanPham.getNhaCungCap() != null ? sanPham.getNhaCungCap().getTenNCC() : null, sanPham.getGiaThanh(),
						sanPham.getPhanLoai(), sanPham.getSoLuong(), sanPham.getTheLoai()});

			});
		}

		currentIndex  = minIndex;

	}
	
	
	
	private void XoaRong() {
		txtTen2.setText("");
		txtTen2.requestFocus();
		txtNcc2.setText("");
		txtTacGia2.setText("");
		txtTheLoai2.setText("");
	}
	

	}
	


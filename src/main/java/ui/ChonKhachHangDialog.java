package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

import dao.KhachHangDAO;
import entity.KhachHang;

public class ChonKhachHangDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ColoredButton btnTimKiem;
	private CustomTable tableKhachHang;
	private DefaultTableModel modelKH;
	private ColoredButton btnChon;
	private ColoredButton btnQuayLai;
	private JTextField txtTenKH;
	private JTextField txtDC;
	private JTextField txtSDT;
	private JTextField txtEmail;
	
	private CapNhatHoaDonDialog owner;
	private KhachHangDAO khachHangDAO;
	private List<KhachHang> khachHangs;
	private int currentIndex = 0;
	private JLabel lbPage;
	private ColoredButton btnHome;
	private ColoredButton btnEnd;
	private ColoredButton btnBefore;
	private ColoredButton btnNext;
	private ColoredButton btnThemMoi;

	public ChonKhachHangDialog(CapNhatHoaDonDialog owner) {
		super(owner);
		this.owner = owner;
		setSize(owner.getSize());
		setTitle("Chọn khách hàng");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setIconImage(new ImageIcon("img/customer.png").getImage());
		setLayout(new BorderLayout());
		setLookAndFeel();

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				owner.setEnabled(true);
			}
			@Override
			public void windowActivated(WindowEvent e) {
				owner.setEnabled(false);
			}
		});
		getContentPane().setBackground(Color.white);
		getContentPane().add(Box.createHorizontalStrut(10), BorderLayout.WEST);
		getContentPane().add(Box.createHorizontalStrut(10), BorderLayout.EAST);
		getContentPane().add(Box.createVerticalStrut(10), BorderLayout.SOUTH);
		
		JPanel pnlMain;
		getContentPane().add(pnlMain = new JPanel(new BorderLayout()));
		pnlMain.setOpaque(false);
		
		Box boxNorth;
		pnlMain.add(boxNorth = Box.createHorizontalBox(), BorderLayout.NORTH);

		JLabel lbTen = new JLabel("Họ tên");
		lbTen.setFont(UIConstant.NORMAL_FONT);
		txtTenKH = new JTextField();
		txtTenKH.setFont(UIConstant.NORMAL_FONT);

		JLabel lbDC = new JLabel("Địa chỉ");
		lbDC.setFont(UIConstant.NORMAL_FONT);
		txtDC = new JTextField();
		txtDC.setFont(UIConstant.NORMAL_FONT);

		JLabel lbSDT = new JLabel("Số điện thoại");
		lbSDT.setFont(UIConstant.NORMAL_FONT);
		txtSDT = new JTextField();
		txtSDT.setFont(UIConstant.NORMAL_FONT);

		JLabel lbEmail = new JLabel("Email");
		lbEmail.setFont(UIConstant.NORMAL_FONT);
		txtEmail = new JTextField();
		txtEmail.setFont(UIConstant.NORMAL_FONT);
		
		lbTen.setPreferredSize(new Dimension(100, 20));
		lbDC.setPreferredSize(new Dimension(100, 20));
		lbSDT.setPreferredSize(new Dimension(100, 20));
		lbEmail.setPreferredSize(new Dimension(100, 20));
		
		Box boxTT = Box.createVerticalBox();
		
		Box boxTenVaDC = Box.createHorizontalBox();
		boxTenVaDC.add(Box.createHorizontalGlue());
		boxTenVaDC.add(Box.createHorizontalStrut(5));
		boxTenVaDC.add(lbTen);
		boxTenVaDC.add(txtTenKH);
		boxTenVaDC.add(Box.createHorizontalStrut(5));
		boxTenVaDC.add(lbDC);
		boxTenVaDC.add(txtDC);
		boxTenVaDC.add(Box.createHorizontalStrut(5));
		boxTenVaDC.add(Box.createHorizontalGlue());
		
		Box boxSDTVaEmail = Box.createHorizontalBox();
		boxSDTVaEmail.add(Box.createHorizontalGlue());
		boxSDTVaEmail.add(Box.createHorizontalStrut(5));
		boxSDTVaEmail.add(lbSDT);
		boxSDTVaEmail.add(txtSDT);
		boxSDTVaEmail.add(Box.createHorizontalStrut(5));
		boxSDTVaEmail.add(lbEmail);
		boxSDTVaEmail.add(txtEmail);
		boxSDTVaEmail.add(Box.createHorizontalStrut(5));
		boxSDTVaEmail.add(Box.createHorizontalGlue());
		
		Box boxTim = Box.createHorizontalBox();
		
		boxTT.add(boxTenVaDC);
		boxTT.add(Box.createVerticalStrut(5));
		boxTT.add(boxSDTVaEmail);
		boxTT.add(Box.createVerticalStrut(5));
		boxTT.add(boxTim);
		
		boxNorth.add(boxTT);
		
		btnTimKiem = new ColoredButton("Tìm", new ImageIcon("img/search.png"));
		btnTimKiem.setFont(UIConstant.NORMAL_FONT);
		btnTimKiem.setBackground(UIConstant.PRIMARY_COLOR);
		btnTimKiem.setForeground(Color.white);
		btnTimKiem.setBorderRadius(30);
		
		btnThemMoi = new ColoredButton("Thêm khách hàng mới", new ImageIcon("img/add.png"));
		btnThemMoi.setFont(UIConstant.NORMAL_FONT);
		btnThemMoi.setBackground(UIConstant.PRIMARY_COLOR);
		btnThemMoi.setForeground(Color.white);
		btnThemMoi.setBorderRadius(30);
		boxTim.add(Box.createHorizontalGlue());
		boxTim.add(Box.createHorizontalStrut(5));
		boxTim.add(btnTimKiem);
		boxTim.add(Box.createHorizontalStrut(5));
		boxTim.add(btnThemMoi);
		boxTim.add(Box.createHorizontalStrut(5));
		boxTim.add(Box.createHorizontalGlue());
		
		tableKhachHang = new CustomTable();
		tableKhachHang.setModel(modelKH = new DefaultTableModel(new String[] {"Mã khách hàng", "Tên khách hàng", "Ngày sinh", "Giới tính", "Địa chỉ", "Email", "SĐT"}, 0));
				
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.setOpaque(false);
		
		JPanel pnlCenter = new JPanel(new BorderLayout());
		
		pnlMain.add(pnlCenter, BorderLayout.CENTER);
		
		JScrollPane scroll;
		tabPane.addTab("Danh sách khách hàng", scroll = new JScrollPane(tableKhachHang));
		scroll.getViewport().setBackground(Color.white);
		scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		
		pnlCenter.add(scroll, BorderLayout.CENTER);
		
		lbPage = new JLabel("Trang 1 trong 1 trang."); lbPage.setFont(UIConstant.NORMAL_FONT);

		btnHome = new ColoredButton(new ImageIcon("img/double_left.png")); btnHome.setBackground(UIConstant.LINE_COLOR);
		btnEnd = new ColoredButton(new ImageIcon("img/double_right.png")); btnEnd.setBackground(UIConstant.LINE_COLOR);
		btnBefore = new ColoredButton(new ImageIcon("img/left.png")); btnBefore.setBackground(UIConstant.LINE_COLOR);
		btnNext = new ColoredButton(new ImageIcon("img/right.png")); btnNext.setBackground(UIConstant.LINE_COLOR);

		btnHome.setToolTipText("Trang đầu");
		btnHome.setBorderRadius(30);
		btnEnd.setToolTipText("Trang cuối");
		btnEnd.setBorderRadius(30);
		btnBefore.setToolTipText("Trang trước");
		btnBefore.setBorderRadius(30);
		btnNext.setToolTipText("Trang kế tiếp");
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
		
		btnChon = new ColoredButton("Chọn", new ImageIcon("img/add.png"));
		btnChon.setBackground(UIConstant.DANGER_COLOR);
		btnChon.setBorderRadius(30);
		btnQuayLai = new ColoredButton("Quay lại", new ImageIcon("img/back.png"));
		btnQuayLai.setBackground(UIConstant.WARNING_COLOR);
		btnQuayLai.setBorderRadius(30);
		
		Box boxButtonCTHD;
		pnlMain.add(boxButtonCTHD = Box.createHorizontalBox(), BorderLayout.SOUTH);
		
		boxButtonCTHD.add(Box.createHorizontalGlue());
		boxButtonCTHD.add(btnChon);
		boxButtonCTHD.add(Box.createHorizontalGlue());
		boxButtonCTHD.add(btnQuayLai);
		boxButtonCTHD.add(Box.createHorizontalGlue());
		
		addEvent();
		
		khachHangDAO = new KhachHangDAO();
	}
	
	public void taiDuLieuLenBang(List<KhachHang> dsKhachHang, int minIndex) {
		if(minIndex >= dsKhachHang.size() || minIndex < 0)
			return;
		
		lbPage.setText("Trang " + (minIndex / 20 + 1) + " trong " + ((dsKhachHang.size() - 1) / 20 + 1) + " trang.");
		modelKH.setRowCount(0);

		for(int i = minIndex; i < minIndex + 20; i++) {
			if(i >= dsKhachHang.size())
				break;
			
			KhachHang item = dsKhachHang.get(i);
			modelKH.addRow(new Object[] {
					item.getMaKhachHang(), item.getTenKhachHang(),
					item.getNgaySinh().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),	item.getDiaChi(), item.isGioiTinh() ? "Nam" : "Nữ",
					item.getEmail(),
					item.getSoDienThoai()
			});
		}
		
		currentIndex = minIndex;
	}
	
	private void timKhachHang() {
		modelKH.setRowCount(0);
		modelKH.fireTableDataChanged();
		khachHangs = khachHangDAO.timKhachHang(txtTenKH.getText(), txtDC.getText(), txtEmail.getText(), txtSDT.getText());
		
		taiDuLieuLenBang(khachHangs, 0);
	}

	private void addEvent() {
		btnThemMoi.addActionListener(e -> {
			themKhachHangMoi();
		});
		
		txtTenKH.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timKhachHang();
			}
		});
		txtDC.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timKhachHang();
			}
		});
		txtEmail.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timKhachHang();
			}
		});
		txtSDT.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				timKhachHang();
			}
		});
		
		btnHome.addActionListener(e -> {
			if(khachHangs != null) {
				taiDuLieuLenBang(khachHangs, 0);
			}
		});

		btnEnd.addActionListener(e -> {
			if(khachHangs != null) {
				taiDuLieuLenBang(khachHangs, khachHangs.size() - khachHangs.size() % 20);
			}
		});

		btnBefore.addActionListener(e -> {
			if(khachHangs != null) {
				taiDuLieuLenBang(khachHangs, currentIndex   - 20);
			}
		});

		btnNext.addActionListener(e -> {
			if(khachHangs != null) {
				taiDuLieuLenBang(khachHangs, currentIndex + 20);
			}
		});
		
		btnChon.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = tableKhachHang.getSelectedRow();
				if(row == -1) {
					UIConstant.showWarning(ChonKhachHangDialog.this, "Chưa chọn thuốc!");
					return;
				}
				
				KhachHang kh = khachHangs.get(row + currentIndex);
				
				owner.setKhachHangDuocChon(kh);
				
				ChonKhachHangDialog.this.dispose();
				owner.setEnabled(true);
				owner.setVisible(true);

			}
		});
		
		btnTimKiem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				timKhachHang();
			}
		});
		
		btnQuayLai.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ChonKhachHangDialog.this.dispose();
				owner.setEnabled(true);
				owner.setVisible(true);
			}
		});
	}

	private void themKhachHangMoi() {
		this.dispose();
		owner.themKhachHangMoi();
		
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

package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.toedter.calendar.JDateChooser;

import dao.HoaDonDAO;
import dao.SanPhamDao;
import entity.HoaDon;
import entity.SanPham;

import javax.swing.UIManager.LookAndFeelInfo;

public class ThongKePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JDateChooser dateFrom;
	private JDateChooser dateTo;
	private ColoredButton btnThongKe;
	private ColoredButton btnQuayLai;
	private ColoredButton btnXoaRong;
	private HoaDonDAO hoaDonDAO;
	private CustomTable tableKetQua;
	private DefaultTableModel modelKQ;
	private JScrollPane scroll;
	private JLabel lbSoHoaDon;
	private JLabel lbTongDoanhThu;
	private NumberFormat nf = NumberFormat.getInstance(Locale.US);
	private MainFrame mainFrame;
	private JLabel lbPage;
	private ColoredButton btnHome;
	private ColoredButton btnEnd;
	private ColoredButton btnBefore;
	private ColoredButton btnNext;
	private List<HoaDon> dsHoaDon;
	private int currentIndex = 0;
	private JTabbedPane tabPane;
	private JCheckBox ckbHetHan;
	private JCheckBox ckbNgungBan;
	private JCheckBox ckbChuaBanLanNao;
	private ColoredButton btnThongKeThuoc;
	private JLabel lbTongHetHan;
	private JLabel lbTongNgungBan;
	private JLabel lbTongChuaBan;
	private CustomTable tableSanPham;
	private DefaultTableModel modelSanPham;
	private JLabel lbPageThuoc;
	private ColoredButton btnHomeSanPham;
	private ColoredButton btnEndSanPham;
	private ColoredButton btnBeforeSanPham;
	private ColoredButton btnNextSanPham;
	private SanPhamDao sanPhamDAO;
	private List<SanPham> dsSanPham;
	private int currentThuocIndex;

	public ThongKePanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setLookAndFeel();

		setOpaque(true);
		setBackground(Color.white);
		setLayout(new BorderLayout());
		
		tabPane = new JTabbedPane();
		tabPane.setOpaque(false);
		tabPane.setBorder(null);
		this.add(tabPane);
		
		JPanel pnlTKDT = new JPanel(new BorderLayout());
		pnlTKDT.setOpaque(false);
		tabPane.addTab("Thống kê doanh thu", pnlTKDT);

		JPanel pnlNorth;
		pnlTKDT.add(pnlNorth = new JPanel(new BorderLayout()), BorderLayout.NORTH);
		pnlNorth.setOpaque(false);

		addNorthTKDT(pnlNorth);

		JPanel pnlCenter;
		pnlTKDT.add(pnlCenter = new JPanel(new BorderLayout()), BorderLayout.CENTER);
		pnlCenter.setOpaque(false);

		addCenterTKDT(pnlCenter);

		JPanel pnlSouth = new JPanel(new BorderLayout());
		this.add(pnlSouth, BorderLayout.SOUTH);
		pnlSouth.setOpaque(false);

		addSouth(pnlSouth);
		
		JPanel pnlTKThuoc = new JPanel(new BorderLayout());
		pnlTKThuoc.setOpaque(false);
		tabPane.addTab("Thống kê ", pnlTKThuoc);
		
		JPanel pnlNorthTKThuoc;
		pnlTKThuoc.add(pnlNorthTKThuoc = new JPanel(new BorderLayout()), BorderLayout.NORTH);
		pnlNorthTKThuoc.setOpaque(false);

		addNorthTKThuoc(pnlNorthTKThuoc);

		JPanel pnlCenterTKThuoc;
		pnlTKThuoc.add(pnlCenterTKThuoc = new JPanel(new BorderLayout()), BorderLayout.CENTER);
		pnlCenterTKThuoc.setOpaque(false);

		addCenterTKThuoc(pnlCenterTKThuoc);

		JPanel pnlSouthTKThuoc = new JPanel(new BorderLayout());
		pnlTKThuoc.add(pnlSouthTKThuoc, BorderLayout.SOUTH);
		pnlSouthTKThuoc.setOpaque(false);

		addEvent();

		hoaDonDAO = new HoaDonDAO();
		sanPhamDAO = new SanPhamDao();
	}

	private void addCenterTKThuoc(JPanel pnlCenter) {
		tableSanPham = new CustomTable();
		JScrollPane scroll = new JScrollPane(tableSanPham);
		scroll.getViewport().setOpaque(false);
		scroll.setBorder(BorderFactory.createTitledBorder(""));

		pnlCenter.add(scroll, BorderLayout.CENTER);

		tableSanPham.setModel(modelSanPham = new DefaultTableModel(new String[] {"Hình ảnh", "Mã thuốc", 
				"Tên thuốc", "Nhà cung cấp", "Đóng gói", "Dạng bào chế", "Đơn vị tính", 
				"Đơn giá", "Phân loại", "Nước sản xuất", "Trạng thái"}, 0));
		tableSanPham.getColumn("Hình ảnh").setCellRenderer(new TableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				if(column == 0) {
					if(value instanceof String)
						return new ImageItem(new ImageIcon(value.toString()));
					else
						return new ImageItem((byte[])value);
				}

				return (Component) value;

			}
		});
		tableSanPham.setRowHeight(60);
		tableSanPham.getColumn("Hình ảnh").setMaxWidth(80);
		tableSanPham.getColumn("Hình ảnh").setMinWidth(80);
		tableSanPham.setFont(UIConstant.NORMAL_FONT);

		lbPageThuoc = new JLabel("Trang 1 trong 1 trang."); lbPageThuoc.setFont(UIConstant.NORMAL_FONT);

		btnHomeSanPham = new ColoredButton(new ImageIcon("Images/double_left.png")); btnHomeSanPham.setBackground(UIConstant.LINE_COLOR);
		btnEndSanPham = new ColoredButton(new ImageIcon("Images/double_right.png")); btnEndSanPham.setBackground(UIConstant.LINE_COLOR);
		btnBeforeSanPham = new ColoredButton(new ImageIcon("Images/left.png")); 		btnBeforeSanPham.setBackground(UIConstant.LINE_COLOR);
		btnNextSanPham = new ColoredButton(new ImageIcon("Images/right.png")); btnNextSanPham.setBackground(UIConstant.LINE_COLOR);

		btnHomeSanPham.setToolTipText("Trang đầu");
		btnEndSanPham.setToolTipText("Trang cuối");
		btnBeforeSanPham.setToolTipText("Trang trước");
		btnNextSanPham.setToolTipText("Trang kế tiếp");

		Box boxPage = Box.createHorizontalBox();
		boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnHomeSanPham); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnEndSanPham); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnBeforeSanPham); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnNextSanPham); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(Box.createHorizontalGlue());
		boxPage.add(lbPageThuoc); boxPage.add(Box.createHorizontalStrut(5));

		pnlCenter.add(boxPage, BorderLayout.SOUTH);
	}

	private void addNorthTKThuoc(JPanel pnlNorth) {
		JLabel lbOrder = new JLabel("Thống kê thuốc");
		lbOrder.setFont(new Font("Tahoma", Font.BOLD, 20));
		lbOrder.setForeground(UIConstant.PRIMARY_COLOR);
		lbOrder.setHorizontalAlignment(JLabel.CENTER);

		pnlNorth.add(lbOrder, BorderLayout.NORTH);

		Box boxY = Box.createVerticalBox();

		pnlNorth.add(boxY, BorderLayout.SOUTH);

		JLabel lbName = new JLabel("Chọn tiêu chí thống kê:");
		lbName.setForeground(new Color(0x646464));
		lbName.setFont(UIConstant.NORMAL_FONT);

		ckbHetHan = new JCheckBox("Thuốc hết hạn"); ckbHetHan.setFont(UIConstant.NORMAL_FONT); ckbHetHan.setOpaque(false);
		ckbNgungBan = new JCheckBox("Thuốc ngừng bán"); ckbNgungBan.setFont(UIConstant.NORMAL_FONT); ckbNgungBan.setOpaque(false);
		ckbChuaBanLanNao = new JCheckBox("Thuốc chưa bán lần nào"); ckbChuaBanLanNao.setFont(UIConstant.NORMAL_FONT); ckbChuaBanLanNao.setOpaque(false);
		
		JPanel pnlTieuChi = new JPanel(new BorderLayout());
		pnlTieuChi.setBackground(UIConstant.BELOW_COLOR);
		pnlTieuChi.setBorder(BorderFactory.createLineBorder(new Color(0xEBE9E9)));
		pnlTieuChi.add(Box.createVerticalStrut(10), BorderLayout.NORTH);
		pnlTieuChi.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);
		
		pnlTieuChi.add(lbName, BorderLayout.NORTH);

		Box boxTieuChi = Box.createHorizontalBox();
		boxTieuChi.add(Box.createHorizontalGlue());
		boxTieuChi.add(Box.createHorizontalStrut(10));
		boxTieuChi.add(ckbHetHan);
		boxTieuChi.add(Box.createHorizontalStrut(10));
		boxTieuChi.add(ckbNgungBan);
		boxTieuChi.add(Box.createHorizontalStrut(10));
		boxTieuChi.add(ckbChuaBanLanNao);
		boxTieuChi.add(Box.createHorizontalStrut(10));
		boxTieuChi.add(Box.createHorizontalGlue());

		pnlTieuChi.add(boxTieuChi, BorderLayout.CENTER);

		Box boxBtn = Box.createHorizontalBox();
		boxBtn.add(Box.createHorizontalGlue());
		boxBtn.add(btnThongKeThuoc = new ColoredButton("Thống kê", new ImageIcon("Images/statistic.png")));
		boxBtn.add(Box.createHorizontalGlue());

		btnThongKeThuoc.setBackground(UIConstant.PRIMARY_COLOR);
		btnThongKeThuoc.setFont(UIConstant.NORMAL_FONT);

		boxY.add(Box.createVerticalStrut(5));
		boxY.add(pnlTieuChi);
		boxY.add(Box.createVerticalStrut(10));
		boxY.add(boxBtn);
		boxY.add(Box.createVerticalStrut(10));

		lbTongHetHan = addItem(boxY, "Tổng thuốc hết hạn");
		lbTongNgungBan = addItem(boxY, "Tổng thuốc đã ngừng bán");
		lbTongChuaBan = addItem(boxY, "Tổng thuốc chưa bán được lần nào");
	}

	private void taiDuLieuLenBang(List<HoaDon> dsHD, int minIndex) {
		if(minIndex >= dsHD.size() || minIndex < 0)
			return;

		lbPage.setText("Trang " + (minIndex / 20 + 1) + " trong " + ((dsHD.size() - 1) / 20 + 1) + " trang.");

		modelKQ.setRowCount(0);
		dsHoaDon = dsHD;
		modelKQ.getDataVector().removeAllElements();
		modelKQ.fireTableDataChanged();
		NumberFormat nf = NumberFormat.getInstance(Locale.US);
		nf.setMinimumIntegerDigits(2);
		nf.setMaximumFractionDigits(2);

		for(int i = minIndex; i < minIndex + 20; i++) {
			if(i >= dsHD.size())
				break;
			HoaDon hd = dsHD.get(i);
			SwingUtilities.invokeLater(() -> {
				modelKQ.addRow(new Object[] {
						hd.getMaHoaDon(), hd.getKhachHang() == null ? null : hd.getKhachHang().getTenKhachHang(),
								hd.getNhanVienLap() == null ? null : hd.getNhanVienLap().getTenNhanVien(), hd.getNgayLap().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
										nf.format(hoaDonDAO.tinhTongTien(hd))
				});
			});
		}

		currentIndex  = minIndex;

	}
	
	private void taiDuLieuLenBangThuoc(List<SanPham> dsSanPham, int minIndex) {
		if(minIndex >= dsSanPham.size() || minIndex < 0)
			return;

		lbPageThuoc.setText("Trang " + (minIndex / 20 + 1) + " trong " +( (dsSanPham.size() - 1) / 20 + 1) + " trang.");

		modelSanPham.setRowCount(0);
		this.dsSanPham = dsSanPham;
		modelSanPham.getDataVector().removeAllElements();
		modelSanPham.fireTableDataChanged();

		for(int i = minIndex; i < minIndex + 20; i++) {
			if(i >= dsSanPham.size())
				break;
			SanPham sanpham = dsSanPham.get(i);
			SwingUtilities.invokeLater(() -> {
				String ha = "";
				byte[] b = null;
				if(sanpham.getHinhAnh() == null)
					ha =  "Images/image.png";
				else {
					try {
						Blob blob = sanpham.getHinhAnh();

						b = blob.getBytes(1, (int) blob.length());

					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				} 
				modelSanPham.addRow(new Object[] {ha != "" ? ha : b , sanpham.getMaSanPham(),sanpham.getTenSanPham(), sanpham.getNhaCungCap() != null ? sanpham.getNhaCungCap().getTenNCC() : null,
						sanpham.getGiaThanh(), sanpham.getPhanLoai()});

			
			});
		}

		currentThuocIndex  = minIndex;

	}

	private void addEvent() {
		btnHomeSanPham.addActionListener(e -> {
			if(dsHoaDon != null) {
				taiDuLieuLenBangThuoc(dsSanPham, 0);
			}
		});

		btnEndSanPham.addActionListener(e -> {
			if(dsHoaDon != null) {
				taiDuLieuLenBangThuoc(dsSanPham, dsSanPham.size() - dsSanPham.size() % 20);
			}
		});

		btnBeforeSanPham.addActionListener(e -> {
			if(dsHoaDon != null) {
				taiDuLieuLenBangThuoc(dsSanPham, currentThuocIndex  - 20);
			}
		});

		btnNextSanPham.addActionListener(e -> {
			if(dsHoaDon != null) {
				taiDuLieuLenBangThuoc(dsSanPham, currentThuocIndex + 20);
			}
		});
		
		//
		btnHome.addActionListener(e -> {
			if(dsHoaDon != null) {
				taiDuLieuLenBang(dsHoaDon, 0);
			}
		});

		btnEnd.addActionListener(e -> {
			if(dsHoaDon != null) {
				taiDuLieuLenBang(dsHoaDon, dsHoaDon.size() - dsHoaDon.size() % 20);
			}
		});

		btnBefore.addActionListener(e -> {
			if(dsHoaDon != null) {
				taiDuLieuLenBang(dsHoaDon, currentIndex  - 20);
			}
		});

		btnNext.addActionListener(e -> {
			if(dsHoaDon != null) {
				taiDuLieuLenBang(dsHoaDon, currentIndex + 20);
			}
		});

		btnQuayLai.addActionListener((e) -> {
			mainFrame.changeCenter(mainFrame.getTrangChuPanel());
		});

		btnThongKe.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				LocalDate fromDate = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(dateFrom.getDate()));
				LocalDate toDate = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(dateTo.getDate()));

				try {
					Object[] kq = hoaDonDAO.thongKeHoaDon(fromDate, toDate);
					
					@SuppressWarnings("unchecked")
					List<HoaDon> list = (List<HoaDon>) kq[0];
					double tongTien = (double) kq[1];

					if(list.size() > 0) {

						modelKQ.setRowCount(0);
						scroll.setBorder(BorderFactory.createTitledBorder("Có " + list.size() + " kết quả."));

						nf.setMinimumIntegerDigits(2);
						nf.setMaximumFractionDigits(2);

						taiDuLieuLenBang(list, 0);

						lbSoHoaDon.setText(list.size() + "");
						lbTongDoanhThu.setText(nf.format(tongTien) + "");

					} else {
						scroll.setBorder(BorderFactory.createTitledBorder("Không có kết quả!"));
						lbSoHoaDon.setText("0");
						lbTongDoanhThu.setText("0");
					}
					
				} catch (Exception e2) {
					
				}
			}
		});
		
		btnThongKeThuoc.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isHetHan = ckbHetHan.isSelected();
				boolean isNgungBan = ckbNgungBan.isSelected();
				boolean isChuaBanLanNao = ckbChuaBanLanNao.isSelected();
				
				if(!isHetHan && !isNgungBan && !isChuaBanLanNao) {
					dsSanPham = null;
					modelSanPham.setRowCount(0);
					lbTongHetHan.setText("0");
					lbTongNgungBan.setText("0");
					lbTongChuaBan.setText("0");
					return;
				}
				
//				Object[] kq = thuocDAO.thongKeThuoc(isHetHan, isNgungBan, isChuaBanLanNao);
				
//				int soHetHan = (int) kq[0];
//				int soNgungBan = (int) kq[1];
//				int soChuaBanLanNao = (int) kq[2];
//				
//				@SuppressWarnings("unchecked")
//				List<SanPham> list = (List<SanPham>) kq[3];

//				if(list.size() > 0) {
//
//					modelKQ.setRowCount(0);
//					scroll.setBorder(BorderFactory.createTitledBorder("Có " + list.size() + " kết quả."));
//
//					nf.setMinimumIntegerDigits(2);
//					nf.setMaximumFractionDigits(2);
//
//					taiDuLieuLenBangThuoc(list, 0);
//
//				}
//				
//				lbTongHetHan.setText(soHetHan + "");
//				lbTongNgungBan.setText(soNgungBan + "");
//				lbTongChuaBan.setText(soChuaBanLanNao + "");
			}
		});
		
		btnXoaRong.addActionListener(e -> {
			ckbHetHan.setSelected(false);
			ckbNgungBan.setSelected(false);
			ckbChuaBanLanNao.setSelected(false);
			
			dateFrom.setDate(new Date());
			dateTo.setDate(new Date());
			
		});
	}

	private void addSouth(JPanel pnlSouth) {
		btnQuayLai = new ColoredButton("Quay lại", new ImageIcon("Images/back.png"));
		btnQuayLai.setBackground(UIConstant.PRIMARY_COLOR);

		btnXoaRong = new ColoredButton("Xóa rỗng", new ImageIcon("Images/empty.png"));
		btnXoaRong.setBackground(UIConstant.WARNING_COLOR);
		btnXoaRong.setToolTipText("Làm rỗng toàn bộ");

		Box boxButtonCTHD;
		pnlSouth.add(boxButtonCTHD = Box.createHorizontalBox(), BorderLayout.CENTER);

		boxButtonCTHD.add(Box.createHorizontalGlue());
		boxButtonCTHD.add(btnXoaRong);
		boxButtonCTHD.add(Box.createHorizontalGlue());
		boxButtonCTHD.add(btnQuayLai);
		boxButtonCTHD.add(Box.createHorizontalGlue());

		pnlSouth.add(Box.createVerticalStrut(5), BorderLayout.NORTH);
		pnlSouth.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
	}

	private void addCenterTKDT(JPanel pnlCenter) {
		tableKetQua = new CustomTable();
		scroll = new JScrollPane(tableKetQua);
		scroll.getViewport().setOpaque(false);
		scroll.setBorder(BorderFactory.createTitledBorder(""));

		pnlCenter.add(scroll, BorderLayout.CENTER);

		tableKetQua.setModel(modelKQ = new DefaultTableModel(
				new String[] {"Mã hóa đơn", "Khách hàng", "Nhân viên lập", "Ngày lập", "Tổng tiền"}
				, 0));

		lbPage = new JLabel("Trang 1 trong 1 trang."); lbPage.setFont(UIConstant.NORMAL_FONT);

		btnHome = new ColoredButton(new ImageIcon("Images/double_left.png")); btnHome.setBackground(UIConstant.LINE_COLOR);
		btnEnd = new ColoredButton(new ImageIcon("Images/double_right.png")); btnEnd.setBackground(UIConstant.LINE_COLOR);
		btnBefore = new ColoredButton(new ImageIcon("Images/left.png")); btnBefore.setBackground(UIConstant.LINE_COLOR);
		btnNext = new ColoredButton(new ImageIcon("Images/right.png")); btnNext.setBackground(UIConstant.LINE_COLOR);

		btnHome.setToolTipText("Trang đầu");
		btnEnd.setToolTipText("Trang cuối");
		btnBefore.setToolTipText("Trang trước");
		btnNext.setToolTipText("Trang kế tiếp");

		Box boxPage = Box.createHorizontalBox();
		boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnHome); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnBefore); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnNext); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(btnEnd); boxPage.add(Box.createHorizontalStrut(5));
		boxPage.add(Box.createHorizontalGlue());
		boxPage.add(lbPage); boxPage.add(Box.createHorizontalStrut(5));

		pnlCenter.add(boxPage, BorderLayout.SOUTH);
	}

	private JLabel addItem(Box parent, String name) {
		Box box = Box.createHorizontalBox();

		parent.add(box);
		parent.add(Box.createVerticalStrut(10));

		box.setOpaque(true);
		box.setBackground(UIConstant.BELOW_COLOR);
		box.setBorder(BorderFactory.createLineBorder(new Color(0xEBE9E9)));

		JLabel lbName = new JLabel(name);
		lbName.setForeground(new Color(0x646464));
		lbName.setFont(UIConstant.NORMAL_FONT);

		JLabel lbKQ = new JLabel("0");
		lbKQ.setForeground(new Color(0x646464));
		lbKQ.setFont(UIConstant.NORMAL_FONT);

		Box boxPage = Box.createVerticalBox();

		box.add(Box.createHorizontalStrut(20));
		box.add(boxPage);
		box.add(Box.createHorizontalGlue());
		box.add(lbKQ);
		box.add(Box.createHorizontalStrut(20));

		boxPage.add(Box.createVerticalStrut(10));
		boxPage.add(lbName);
		boxPage.add(Box.createVerticalStrut(10));

		return lbKQ;
	}

	private void addNorthTKDT(JPanel pnlNorth) {
		JLabel lbOrder = new JLabel("Thống kê doanh thu");
		lbOrder.setFont(new Font("Tahoma", Font.BOLD, 20));
		lbOrder.setForeground(UIConstant.PRIMARY_COLOR);
		lbOrder.setHorizontalAlignment(JLabel.CENTER);

		pnlNorth.add(lbOrder, BorderLayout.NORTH);

		Box boxY = Box.createVerticalBox();

		pnlNorth.add(boxY, BorderLayout.SOUTH);

		JLabel lbName = new JLabel("Chọn khoảng thời gian cần thống kê:");
		lbName.setForeground(new Color(0x646464));
		lbName.setFont(UIConstant.NORMAL_FONT);

		JLabel lbFrom = new JLabel("Từ ngày");
		lbFrom.setForeground(new Color(0x646464));
		lbFrom.setFont(UIConstant.NORMAL_FONT);

		JLabel lbTo = new JLabel("Đến ngày");
		lbTo.setForeground(new Color(0x646464));
		lbTo.setFont(UIConstant.NORMAL_FONT);

		dateFrom = new JDateChooser(new Date());
		dateFrom.setDateFormatString("dd-MM-yyyy");
		dateFrom.setFont(UIConstant.NORMAL_FONT);
		dateTo = new JDateChooser(new Date());
		dateTo.setDateFormatString("dd-MM-yyyy");
		dateTo.setFont(UIConstant.NORMAL_FONT);

		JPanel pnlDate = new JPanel(new BorderLayout());
		pnlDate.setBackground(UIConstant.BELOW_COLOR);
		pnlDate.setBorder(BorderFactory.createLineBorder(new Color(0xEBE9E9)));
		pnlDate.add(Box.createVerticalStrut(10), BorderLayout.NORTH);
		pnlDate.add(Box.createVerticalStrut(10), BorderLayout.SOUTH);

		Box boxDate = Box.createHorizontalBox();
		boxDate.add(Box.createHorizontalStrut(20));
		boxDate.add(lbName);
		boxDate.add(Box.createHorizontalStrut(10));
		boxDate.add(lbFrom);
		boxDate.add(Box.createHorizontalStrut(10));
		boxDate.add(dateFrom);
		boxDate.add(Box.createHorizontalStrut(20));
		boxDate.add(lbTo);
		boxDate.add(Box.createHorizontalStrut(10));
		boxDate.add(dateTo);
		boxDate.add(Box.createHorizontalStrut(20));

		pnlDate.add(boxDate, BorderLayout.CENTER);

		Box boxBtn = Box.createHorizontalBox();
		boxBtn.add(Box.createHorizontalGlue());
		boxBtn.add(btnThongKe = new ColoredButton("Thống kê", new ImageIcon("Images/statistic.png")));
		boxBtn.add(Box.createHorizontalGlue());

		btnThongKe.setBackground(UIConstant.PRIMARY_COLOR);
		btnThongKe.setFont(UIConstant.NORMAL_FONT);

		boxY.add(Box.createVerticalStrut(5));
		boxY.add(pnlDate);
		boxY.add(Box.createVerticalStrut(10));
		boxY.add(boxBtn);
		boxY.add(Box.createVerticalStrut(10));

		lbSoHoaDon = addItem(boxY, "Tổng hóa đơn");
		lbTongDoanhThu = addItem(boxY, "Tổng doanh thu");
	}

	private void setLookAndFeel() {
		for(LookAndFeelInfo item : UIManager.getInstalledLookAndFeels()) {
			if(item.getName().equalsIgnoreCase("Windows"))
				try {
					UIManager.setLookAndFeel(item.getClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
		}
	}
}

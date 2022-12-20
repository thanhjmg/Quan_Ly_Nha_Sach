package dao;

import java.util.ArrayList;
import java.util.List;



import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;

import entity.NhaCungCap;
import entity.SanPham;



public class SanPhamDao {

	private SessionFactory sessionFactory;

	public SanPhamDao() {
		super();
		this.sessionFactory= DatabaseConnection.getInstance().getSessionFactory();
	}
	
	public List<SanPham> lay20SanPhamGanDay(){
		List<SanPham> list = new ArrayList<SanPham>();
		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {

			list = session.createNativeQuery("select top 20 * from SanPham order by maSanPham ", SanPham.class).getResultList();
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		return list;
		
	}
	public List<String> getDSPhanLoai() {
		List<String> list = new ArrayList<String>();

		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {

			List<?> resultList = session.createNativeQuery("select phanLoai from Thuoc where phanLoai not like '' group by phanLoai").getResultList();
			for(Object item : resultList) {
				list.add((String)item);
			}

			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}

		return list;
	}
	
	
	public boolean xoaSanPham(String maSanPham) {
		boolean flag = false;
		Session session = sessionFactory.getCurrentSession();
		
		Transaction transaction = session.beginTransaction();
		
		try {
			String sqlQuery = "delete from SanPham where maSanPham = :x";
			session.createNativeQuery(sqlQuery).setParameter("x", maSanPham).executeUpdate();

			transaction.commit();
			flag = true;
			
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return flag;	
	}
	public boolean themSanPham(SanPham sanpham) {
		
		boolean flag = false;

		Session session = sessionFactory.getCurrentSession();

		Transaction transaction = session.beginTransaction();

		try {
			session.save(sanpham);
			transaction.commit();

			flag = true;


		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}

		return flag;
	}
	public boolean capNhatSanPham(SanPham sanPham) {
		boolean flag = false;
		Session session = sessionFactory.getCurrentSession();

		Transaction transaction = session.beginTransaction();

		try {
			session.update(sanPham);

			transaction.commit();

			flag = true;

		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}

		return flag;
	}
		
	
	public List<SanPham> timSanPham(String tenSanPham, String nhaCungCap, String tacGia, String theLoai){
		List<SanPham> result = new ArrayList<SanPham>();
		if(tenSanPham.trim().equals(""))
				tenSanPham= null;
		if(nhaCungCap.trim().equals(""))
			nhaCungCap= null;
		if(tacGia.trim().equals(""))
			tacGia= null;
		if(theLoai.trim().equals(""))
			theLoai= null;
		
		String query = "select * from SanPham ";
		String queryNCC= "select * from SanPham INNER JOIN NhaCungCap ON SanPham.maNCC = NhaCungCap.maNCC ";
		
		if(tenSanPham != null || nhaCungCap != null || tacGia!= null || theLoai != null ) {
			query+="where ";
			queryNCC+="where ";
			
			if(tenSanPham != null)
				query += "tenSanPham like :ten ";
			if(nhaCungCap != null) {
				if(tenSanPham == null) 
				     queryNCC+= "tenNCC like :ncc ";
				else
				     queryNCC+= "and tenNCC like :ncc ";
			}
			if(tacGia != null) {
				if(tenSanPham == null && nhaCungCap == null)
					query += "tacGia like :tg ";
				else
					query += "and tacGia like :tg ";
			}
			if(theLoai != null) {
				if(tenSanPham == null && nhaCungCap == null && tacGia == null)
					query += "theLoai like :tl ";
				else
					query +="and theLoai like :tl ";	
			}
			
		}
		
		Session session = sessionFactory.getCurrentSession();

		Transaction tran = session.beginTransaction();	
		
		try {
			NativeQuery<SanPham> nativeQuery = session.createNativeQuery(query, SanPham.class);
			if(tenSanPham != null)
				nativeQuery.setParameter("ten", "%" + tenSanPham + "%");
//			if(nhaCungCap != null)
//				nativeQuery.setParameter("ncc", "%" + nhaCungCap + "%");
			if(tacGia != null)
				nativeQuery.setParameter("tg", "%" + tacGia + "%");
			if(theLoai != null)
				nativeQuery.setParameter("tl", "%" + theLoai + "%");
			NativeQuery<NhaCungCap> nativeQueryNCC = session.createNativeQuery(queryNCC,NhaCungCap.class);
			if(nhaCungCap != null)
				nativeQueryNCC.setParameter("ncc", "%" + nhaCungCap + "%");
			result = nativeQuery.getResultList();
			tran.commit();
					
		}catch (Exception e) {
			tran.rollback();
			e.printStackTrace();
		}

		return result;
		
	}
	
	
	
	
//	//test
//	public List<SanPham> timSanPham(String tenSanPham, String nhaCungCap, String tacGia, String theLoai, int trangThai){
//	List<SanPham> result = new ArrayList<SanPham>();
//	if(tenSanPham.trim().equals(""))
//			tenSanPham= null;
//	if(nhaCungCap.trim().equals(""))
//		nhaCungCap= null;
//	if(tacGia.trim().equals(""))
//		tacGia= null;
//	if(theLoai.trim().equals(""))
//		theLoai= null;
//	
//	String query = "select * from SanPham ";
//	String queryncc= "select * from SanPham INNER JOIN NhaCungCap ON SanPham.maNCC = NhaCungCap.maNCC ";
//	
//	if(tenSanPham != null || nhaCungCap != null || tacGia!= null || theLoai != null || trangThai != -1) {
//		query+="where ";
//		
//		
//		if(tenSanPham != null)
//			query += "tenSanPham like :ten ";
//		if(nhaCungCap != null) {
//			if(tenSanPham == null) 
//			     query+= "tenNCC like :ncc ";
//			else
//			     query+= "and tenNCC like :ncc ";
//		}
//		if(tacGia != null) {
//			if(tenSanPham == null && nhaCungCap == null)
//				query += "tacGia like :tg ";
//			else
//				query += "and tacGia like :tg ";
//		}
//		if(theLoai != null) {
//			if(tenSanPham == null && nhaCungCap == null && tacGia == null)
//				query += "theLoai like :tl ";
//			else
//				query +="and theLoai like :tl ";	
//		}
//		if(trangThai !=-1) {
//			if(tenSanPham == null && nhaCungCap == null && tacGia == null && theLoai == null)
//				query += "trangThai = " + (trangThai == 1 ? "1" : "0") +" ";
//			else
//				query += "and trangThai = " + (trangThai == 1 ? "1" : "0") + " ";
//		}
//		
//	}
//	
//	Session session = sessionFactory.getCurrentSession();
//
//	Transaction tran = session.beginTransaction();	
//	
//	try {
//		NativeQuery<SanPham> nativeQuery = session.createNativeQuery(query, SanPham.class);
//		if(tenSanPham != null)
//			nativeQuery.setParameter("ten", "%" + tenSanPham + "%");
//		if(nhaCungCap != null)
//			nativeQuery.setParameter("ncc", "%" + nhaCungCap + "%");
//		if(tacGia != null)
//			nativeQuery.setParameter("tg", "%" + tacGia + "%");
//		if(theLoai != null)
//			nativeQuery.setParameter("tl", "%" + theLoai + "%");
//		if(nhaCungCap != null)
//			nativeQuery.setParameter("ncc", "%" + nhaCungCap + "%");
//		result = nativeQuery.getResultList();
//		tran.commit();
//				
//	}catch (Exception e) {
//		tran.rollback();
//		e.printStackTrace();
//	}
//
//	return result;
//	
//}

	
	
	
}

package com.hotelroombooking.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpSession;

import com.hotelroombooking.dao.MeetingHallTransactionDao;
import com.hotelroombooking.message.Mail;
import com.hotelroombooking.message.Mailer;
import com.hotelroombooking.model.Guest;
import com.hotelroombooking.model.MeetingHallDetails;
import com.hotelroombooking.model.MeetingHallTransaction;
import com.hotelroombooking.model.RoomTransaction;
import com.hotelroombooking.model.WeddingHallDetails;
import com.hotelroombooking.model.WeddingHallTransaction;
import com.hotelroombooking.util.ConnectionUtil;

public class MeetingHallTransactionDaoImpl implements MeetingHallTransactionDao
{
	public boolean bookMeetingHall(HttpSession session) 
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		int vacantMeetingRoomNumber=0;
		int guestId=0;
		
		boolean flag=false;
		
		
		try {
//		do {
//		System.out.println("enter check-in date");
//		checkIn = sdf.parse(sc.nextLine());
//		System.out.println("enter check-out date");
//		checkOut = sdf.parse(sc.nextLine());
//		if(checkIn.after(checkOut))
//		{
//			System.out.println("Invalid Date Format");
//			dateFlag=false;
//		}
//		else
//		{
//			dateFlag=true;
//		}
//		}while(dateFlag!=true);
//		System.out.println("enter category");
//		System.out.println("1.Premium\n2.luxury\n3.standard\n4.budget");
//		int categoryChoice = Integer.parseInt(sc.nextLine());
//		String category = (categoryChoice==1)?"premium":(categoryChoice==2)?"luxury":(categoryChoice==3)?"standard":"budget";
//		System.out.println("enter location");
//		String location = sc.nextLine();
			Guest guestObj=(Guest)session.getAttribute("currentUser");
			MeetingHallTransaction meetingHallTransObj=(MeetingHallTransaction)session.getAttribute("bookMeetingHallDetails");
			
		
		String fetchVacantMeetingRoom="select meeting_hall_number from meeting_hall_details where status='vacant' and category=? and location=?";
		
		String bookMeetingRoomQuery="insert into meeting_hall_transaction(meeting_hall_number,check_in,check_out,category,location,guest_id) values(?,?,?,?,?,?)";
		String updateBookMeetingRoomQuery="update meeting_hall_details set status='occupied' where meeting_hall_number=?";
//		System.out.println(bookRoomQuery);
		Connection conn = ConnectionUtil.getDbConnection();
		PreparedStatement pstmt1 = conn.prepareStatement(fetchVacantMeetingRoom);
		
		
		pstmt1.setString(1, meetingHallTransObj.getCategory());
		pstmt1.setString(2, meetingHallTransObj.getLocation());
		
		ResultSet rs = pstmt1.executeQuery();
		if(rs.next())
		{
			vacantMeetingRoomNumber=rs.getInt(1);
		}
		
		PreparedStatement pstmt2 = conn.prepareStatement(bookMeetingRoomQuery);
		PreparedStatement pstmt3 = conn.prepareStatement(updateBookMeetingRoomQuery);
		
		GuestDaoImpl guestDaoObj = new GuestDaoImpl();
		
		guestId=guestDaoObj.findGuestId(guestObj);
//		System.out.println(guestId);c
		meetingHallTransObj.setroomNumber(vacantMeetingRoomNumber);
		
		pstmt2.setInt(1, vacantMeetingRoomNumber);
		pstmt2.setDate(2, new java.sql.Date(sdf.parse(meetingHallTransObj.getCheckIn()).getTime()));
		pstmt2.setDate(3, new java.sql.Date(sdf.parse(meetingHallTransObj.getCheckOut()).getTime()));
		pstmt2.setString(4, meetingHallTransObj.getCategory());
		pstmt2.setString(5,meetingHallTransObj.getLocation());
		pstmt2.setInt(6, guestId);
		
		pstmt3.setInt(1, vacantMeetingRoomNumber);
		
//		MeetingHallTransaction meetingHallTransObj= new MeetingHallTransaction(vacantMeetingRoomNumber,String.valueOf(checkIn),String.valueOf(checkOut),category,location);

		
//		System.out.println(bookRoomQuery);

		
		flag = pstmt2.executeUpdate()>0;
		if(flag)
		{
			System.out.println("Meeting Hall booked");
			pstmt3.executeUpdate();
			Mailer.send("hemnaathrsurya@gmail.com", "hangover@18!!", guestObj.getEmail(), "Hotel Room Booking Application", Mail.bookMeetingHallMail(meetingHallTransObj));

		}
		else
		{
			System.out.println("Error in booking");
		}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return flag;

	}
	
	
	
	public boolean cancelMeetingHall(HttpSession session) 
	{
//		MeetingHallTransaction meetingHallTransObj=null;
		boolean flag=false;
		
		try {
//		Scanner sc = new Scanner(System.in);
//		System.out.println("Enter meeting hall number");
//		int meetingRoomNumber = Integer.parseInt(sc.nextLine());
			
			
		
		String updateCancelRoomQuery = "update meeting_hall_details set status='vacant' where meeting_hall_number=?";
		Connection conn = ConnectionUtil.getDbConnection();
		PreparedStatement pstmt = conn.prepareStatement(updateCancelRoomQuery);
		Guest guestObj=(Guest)session.getAttribute("currentUser");
		MeetingHallTransaction meetingHallTransObj=(MeetingHallTransaction)session.getAttribute("cancelMeetingHallDetails");
				
		pstmt.setInt(1, meetingHallTransObj.getroomNumber());
		
//		meetingHallTransObj= new MeetingHallTransaction(meetingRoomNumber,null,null,null,null);

				
		flag=pstmt.executeUpdate()>0;
		if(flag)
		{
			System.out.println("Booking Cancelled");
			Mailer.send("hemnaathrsurya@gmail.com", "hangover@18!!", guestObj.getEmail(), "Hotel Room Booking Application", Mail.cancelMeetingHallMail(meetingHallTransObj));

		}
		else
		{
			System.out.println("Invalid Room");
		}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return flag;
	}
	
	
	
	public boolean updateMeetingHall(HttpSession session) 
	{
		
		 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Scanner sc = new Scanner(System.in);
		int vacantMeetingHallNumber=0;
		int i=0;
		int guestId=0;
		boolean flag=false;
		Date checkIn;
		Date checkOut;
		
		try {
//		System.out.println("enter meeting hall number");
//		int meetingHallNumber = Integer.parseInt(sc.nextLine());
//		do {
//		System.out.println("enter check-in date");
//		checkIn = sdf.parse(sc.nextLine());
//		System.out.println("enter check-out date");
//		checkOut = sdf.parse(sc.nextLine());
//		if(checkIn.after(checkOut))
//		{
//			System.out.println("Invalid Date Format");
//			dateFlag=false;
//		}
//		else
//		{
//			dateFlag=true;
//		}
//		}while(dateFlag!=true);
//		System.out.println("enter category");
//		System.out.println("1.Premium\n2.luxury\n3.standard\n4.budget");
//		int categoryChoice = Integer.parseInt(sc.nextLine());
//		String category = (categoryChoice==1)?"premium":(categoryChoice==2)?"luxury":(categoryChoice==3)?"standard":"budget";
//		System.out.println("enter location");
//		String location = sc.nextLine();
			
			Guest guestObj=(Guest)session.getAttribute("currentUser");
			MeetingHallTransaction meetingHallTransObj=(MeetingHallTransaction)session.getAttribute("updateMeetingHallDetails");

		
		String updateRoomQuery="update meeting_hall_transaction set check_in=?,check_out=?,category=?,location=? where meeting_hall_number=?";
		
		Connection conn = ConnectionUtil.getDbConnection();
		PreparedStatement pstmt1 = conn.prepareStatement(updateRoomQuery);
		
		pstmt1.setDate(1, new java.sql.Date(sdf.parse(meetingHallTransObj.getCheckIn()).getTime()));
		pstmt1.setDate(2, new java.sql.Date(sdf.parse(meetingHallTransObj.getCheckOut()).getTime()));
		pstmt1.setString(3, meetingHallTransObj.getCategory());
		pstmt1.setString(4, meetingHallTransObj.getLocation());
		pstmt1.setInt(5, meetingHallTransObj.getroomNumber());
		
		pstmt1.executeUpdate();

		
		String fetchVacantRoom="select meeting_hall_number from meeting_hall_details where status='vacant' and category=? and location=?";
		String updateRoomQuery2="update meeting_hall_transaction set meeting_hall_number=? where check_in=? and check_out=? and category=? and location=? and guest_id=?";
		String updateRoomQuery3 = "update meeting_hall_details set status='vacant' where meeting_hall_number=?";
		String updateRoomQuery4 = "update meeting_hall_details set status='occupied' where meeting_hall_number=?";
		
		PreparedStatement pstmt2 = conn.prepareStatement(fetchVacantRoom);
		PreparedStatement pstmt3 = conn.prepareStatement(updateRoomQuery2);
		PreparedStatement pstmt4 = conn.prepareStatement(updateRoomQuery3);
		PreparedStatement pstmt5 = conn.prepareStatement(updateRoomQuery4);
		
		pstmt2.setString(1, meetingHallTransObj.getCategory());
//		System.out.println(category);
		pstmt2.setString(2, meetingHallTransObj.getLocation());
//		System.out.println(location);

		
		ResultSet rs = pstmt2.executeQuery();
		if(rs.next())
		{
			vacantMeetingHallNumber=rs.getInt(1);
			System.out.println(rs.getInt(1));
		}
//		System.out.println(vacantRoomNumber);
		
		GuestDaoImpl guestDaoObj = new GuestDaoImpl();
		guestId=guestDaoObj.findGuestId(guestObj);
		
		pstmt3.setInt(1, vacantMeetingHallNumber);
		pstmt3.setDate(2, new java.sql.Date(sdf.parse(meetingHallTransObj.getCheckIn()).getTime()));
		pstmt3.setDate(3, new java.sql.Date(sdf.parse(meetingHallTransObj.getCheckOut()).getTime()));
		pstmt3.setString(4, meetingHallTransObj.getCategory());
		pstmt3.setString(5, meetingHallTransObj.getLocation());
		pstmt3.setInt(6, guestId);
		
		pstmt3.executeUpdate();
		
//		MeetingHallTransaction meetingHallTransObj = new MeetingHallTransaction(vacantMeetingHallNumber,String.valueOf(checkIn),String.valueOf(checkOut),category,location);
		Mailer.send("hemnaathrsurya@gmail.com", "hangover@18!!", guestObj.getEmail(), "Hotel Room Booking Application", Mail.updateMeetingHallMail(meetingHallTransObj));


		
		pstmt4.setInt(1, meetingHallTransObj.getroomNumber());
		pstmt4.executeUpdate();
		
		meetingHallTransObj.setroomNumber(vacantMeetingHallNumber);
		
		pstmt5.setInt(1, vacantMeetingHallNumber);
		flag=pstmt5.executeUpdate()>0;
		
		if(flag)
		{
			System.out.println("Updated Room details");
		}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return flag;
		
	}
	
		
	
	
	
	public List<MeetingHallTransaction> showMeetingHallBooking(Guest guestObj) 
	{
		int guestId=0;
		List<MeetingHallTransaction> meetingHallBooking = new ArrayList<MeetingHallTransaction>();
		
		try {
		String showMeetingHallBookingQuery = "select * from meeting_hall_transaction where guest_id=?";
		
		Connection conn = ConnectionUtil.getDbConnection();
		PreparedStatement pstmt = conn.prepareStatement(showMeetingHallBookingQuery);
		
		GuestDaoImpl guestDaoObj = new GuestDaoImpl();
		guestId=guestDaoObj.findGuestId(guestObj);
		
		pstmt.setInt(1, guestId);
		
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next())
		{
			MeetingHallTransaction meetingHallTrans = new MeetingHallTransaction(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5));
				meetingHallBooking.add(meetingHallTrans);
		}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		
		return meetingHallBooking;
	}
	
	
	
	
	
	public boolean addMeetingHallAdmin(HttpSession session) 
	{
//		Scanner sc = new Scanner(System.in);
		boolean flag=false;
		
		try {
//		System.out.println("enter meeting hall number");
//		int meetingHallNumber = Integer.parseInt(sc.nextLine());
//		System.out.println("enter meeting hall category");
//		String meetingHallCategory = sc.nextLine();
//		System.out.println("enter meeting hall location");
//		String meetingHallLocation = sc.nextLine();
//		System.out.println("enter meeting hall price");
//		int meetingHallPrice = Integer.parseInt(sc.nextLine());
			MeetingHallDetails meetingHallDetailsObj=(MeetingHallDetails)session.getAttribute("addMeetingHallDetails");
		
		
		String addMeetingHallQuery="insert into meeting_hall_details(meeting_hall_number,category,location,price) values(?,?,?,?)";
		
		Connection conn = ConnectionUtil.getDbConnection();
		PreparedStatement pstmt = conn.prepareStatement(addMeetingHallQuery);
		
		pstmt.setInt(1,meetingHallDetailsObj.getmeetingHallNumber());
		pstmt.setString(2,meetingHallDetailsObj.getCategory());
		pstmt.setString(3,meetingHallDetailsObj.getLocation());
		pstmt.setInt(4,meetingHallDetailsObj.getPrice());
		
		flag=pstmt.executeUpdate()>0;
		if(flag)
		{
			System.out.println("meeting hall added");
		}
		else
		{
			System.out.println("Error");
		}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return flag;
	}
	
	
	
	
	
	
	public void deleteMeetingHallAdmin() 
	{
		Scanner sc = new Scanner(System.in);
		
		try {
		System.out.println("enter wedding hall number");
		int meetingHallNumber = Integer.parseInt(sc.nextLine());
		
		String deleteMeetingHallQuery = "delete from meeting_hall_details where meeting_hall_number=?";
		
		Connection conn = ConnectionUtil.getDbConnection();
		PreparedStatement pstmt = conn.prepareStatement(deleteMeetingHallQuery);
		
		pstmt.setInt(1, meetingHallNumber);
		
		int i=pstmt.executeUpdate();
		if(i>0)
		{
			System.out.println("meeting hall deleted");
		}
		else
		{
			System.err.println("error");
		}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	
	
	
	
	public void updateMeetingHallAdmin() 
	{
		Scanner sc = new Scanner(System.in);
		
		try {
		System.out.println("enter meeting room number");
		int meetingHallNumber = Integer.parseInt(sc.nextLine());
		System.out.println("enter meeting room category");
		String meetingHallCategory = sc.nextLine();
		System.out.println("enter meeting room location");
		String meetingHallLocation = sc.nextLine();
		System.out.println("enter meeting room price");
		int meetingHallPrice = Integer.parseInt(sc.nextLine());
		
		String updateRoomQuery="update meeting_hall_details set category=?,location=?,price=? where meeting_hall_number=?";
		
		Connection conn = ConnectionUtil.getDbConnection();
		PreparedStatement pstmt = conn.prepareStatement(updateRoomQuery);
		
		pstmt.setString(1, meetingHallCategory);
		pstmt.setString(2, meetingHallLocation);
		pstmt.setInt(3, meetingHallPrice);
		pstmt.setInt(4, meetingHallNumber);
		
		int i=pstmt.executeUpdate();
		if(i>0)
		{
			System.out.println("meeting hall updated");
		}
		else
		{
			System.err.println("error");
		}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	
	
	
	
	
	
	
	
	
}
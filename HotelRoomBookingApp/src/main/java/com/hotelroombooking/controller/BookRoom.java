package com.hotelroombooking.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hotelroombooking.daoimpl.RoomTransactionDaoImpl;
import com.hotelroombooking.model.Guest;
import com.hotelroombooking.model.RoomTransaction;

/**
 * Servlet implementation class bookRoom
 */
@WebServlet("/BookRoom")
public class BookRoom extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookRoom() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		String checkIn = request.getParameter("checkIn");
		String checkOut = request.getParameter("checkOut");
		String category = request.getParameter("category");
		String location = request.getParameter("location");
		
		RoomTransaction roomTransObj = new RoomTransaction(0,checkIn,checkOut,category,location);
		RoomTransactionDaoImpl roomTransDaoObj = new RoomTransactionDaoImpl();
		HttpSession session = request.getSession();
		session.setAttribute("bookRoomDetails", roomTransObj);
		roomTransDaoObj.bookRoom(session);
		
		if(session.getAttribute("NoRoomsToBook")!=null) {
			response.sendRedirect("GuestDashboard.jsp");
			
		}
		else {
			response.sendRedirect("BookRoomPayment.jsp");
		}

	}

}

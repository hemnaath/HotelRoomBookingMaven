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
import com.hotelroombooking.model.RoomDetails;

/**
 * Servlet implementation class editRoom
 */
@WebServlet("/editRoom")
public class editRoom extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
   

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		
		int roomNumber = Integer.parseInt(request.getParameter("roomNumber"));
		String category = request.getParameter("category");
		String location = request.getParameter("location");
		int price = Integer.parseInt(request.getParameter("price"));
		
		RoomDetails roomDetailsObj = new RoomDetails(roomNumber,null,category,location,price);
		RoomTransactionDaoImpl roomTransDaoObj = new RoomTransactionDaoImpl();
		HttpSession session = request.getSession();
		session.setAttribute("editRoomDetails", roomDetailsObj);
		boolean flag = roomTransDaoObj.updateRoomAdmin(session);
		PrintWriter pw = response.getWriter();
		pw.write(flag+"");
		
		
		
		
		
		
//		doGet(request, response);
	}

}

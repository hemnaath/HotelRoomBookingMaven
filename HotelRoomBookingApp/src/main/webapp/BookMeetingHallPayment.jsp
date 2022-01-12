<%@page import="com.hotelroombooking.daoimpl.MeetingHallTransactionDaoImpl"%>
<%@page import="com.hotelroombooking.model.MeetingHallTransaction"%>
<%@page import="com.hotelroombooking.daoimpl.WeddingHallTransactionDaoImpl"%>
<%@page import="com.hotelroombooking.model.WeddingHallTransaction"%>
<%@page import="com.hotelroombooking.daoimpl.RoomTransactionDaoImpl"%>
<%@page import="com.hotelroombooking.model.Guest"%>
<%@page import="com.hotelroombooking.model.RoomTransaction"%>
<%@page import="com.hotelroombooking.model.MeetingHallDetails"%>
<%@page import="com.hotelroombooking.model.WeddingHallDetails"%>
<%@page import="com.hotelroombooking.model.RoomDetails"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Payment</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
   <style>
	 .registerForm {
    width: 900px;
    box-shadow: 0px 0px 14px 0px #d9d9d9;
    padding: 10px;
    margin: 7% auto;
    height: 421px;
}
	.formBtn {
	    text-align: center;
	}
	.formBtn input {
	    margin: 10px;
	}
	.registerForm input {
   	 	width: 100%;
	}
	.registerForm .form-group label {
	    width: 100%;
	}
	.formBtn input {
	    display: block;
	    margin: 0px;
	}
	.formcontent input {
    padding: 7px;
    margin-bottom: 10px;
}

.formcontent label {
    margin: 5px 0px;
}
.formcontent {
    padding: 16px;
}
.verticalRule label {
    margin-bottom: 40px;
}

.verticalRule {
    border-right: 1px solid #d9d9d9;
    height: 100%;
    vertical-align: middle;
    padding: 11% 0px;
}
	</style>

</head>
<body>
<%
MeetingHallTransaction meetingHallTransObj1=(MeetingHallTransaction)session.getAttribute("bookMeetingHallDetails");
MeetingHallTransactionDaoImpl meetingHallTransObj = new MeetingHallTransactionDaoImpl();
int price = meetingHallTransObj.findBookMeetingPrice(session);
%>

<div class="registerForm row">	

<div class="col-sm-6 row verticalRule">

<div class="col-sm-12"><label>Meeting Hall Number :</label><%=meetingHallTransObj1.getroomNumber() %></div>
<div class="col-sm-6">
<label>Check-In Date :</label><%=meetingHallTransObj1.getCheckIn() %></div>
<div class="col-sm-6">
<label>Check-Out Date :</label><%=meetingHallTransObj1.getCheckOut() %></div>
<div class="col-sm-6">
<label>Category :</label><%=meetingHallTransObj1.getCategory()%></div>
<div class="col-sm-6">
<label>Location :</label><%=meetingHallTransObj1.getLocation() %></div>
<div class="col-sm-12">
<label>Price :</label><%=price %></div>
</div>

<div class="col-sm-6">
<div class="formcontent">
	<h3 class="text-center">PAYMENT PORTAL</h3>
	<form action="payment" method="post">
		<label>Card Number:</label>
		<input type="text" id="cardNumber" name="cardNumber" placeholder="Card Number" pattern="[0-9]{16}" title="invalid card number"required>
		<label>Expiry Date:</label>
		<input type="text" id="expiryDate" name="expiryDate" placeholder="mm/yy" pattern="[0-9]{2}[/][0-9]{2}" title="invalid expiry date"required>
		<label>CVV:</label>
		<input type="text" id="cvv" name="cvv" placeholder="CVV" pattern="[0-9]{3}" title="invalid cvv"required>
		<button class="btn btn-primary">Pay Now</button>
	</form>
	</div>
	</div>
</div>






</body>
</html>
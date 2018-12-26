<?php 

	require_once('connection.php');

	$content_id = $_REQUEST['content_id'];

	$query = mysqli_query($conn, "SELECT com.*, user.username AS username FROM comments AS com LEFT JOIN user AS user ON com.user_id = user.user_id WHERE com.content_id='$content_id' ORDER BY com.comment_id DESC");

	$result = array();
	while($row = mysqli_fetch_array($query)){

		$datetime = date("d/m/y h:i A", strtotime($row['datetime']));

		array_push($result,array(
			'comment_id'	=> $row['comment_id'],
			'content_id'	=> $row['content_id'],
			'user_id'		=> $row['user_id'],
			'username'		=> $row['username'],
			'content'		=> $row['content'],
			'datetime'		=> $datetime
		));
	}
	
	echo json_encode(array('result'=>$result));
	mysqli_close($conn);

 ?>
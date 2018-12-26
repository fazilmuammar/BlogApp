<?php 

	require_once('connection.php');
	$content_id = $_REQUEST['content_id'];

	$query1 = mysqli_query($conn, "SELECT con.*, cat.name AS category FROM content AS con LEFT JOIN category AS cat ON con.category_id = cat.category_id WHERE con.content_id='$content_id' ");
	$query2 = mysqli_query($conn, "UPDATE content SET view=view+1 WHERE content_id='$content_id'");
	$query3 = mysqli_query($conn, "SELECT user_id FROM love WHERE content_id= '$content_id' ");
	
	$result1 = array();
	$result2 = array();
	
	while($row = mysqli_fetch_array($query1)){
		$created = date("d/m/y h:i A", strtotime($row['created']));
		$updated = date("d/m/y h:i A", strtotime($row['updated']));
		array_push($result1,array(
			
			'content_id'	=> $row['content_id'],
			'category_id'	=> $row['category_id'],
			'category'		=> $row['category'],
			'author'		=> $row['author'],
			'title'			=> $row['title'],
			'image'			=> $row['image'],
			'created'		=> $created,
			'updated'		=> $updated,
			'content'		=> $row['content'],
			'view'			=> $row['view'],
			'love'			=> $row['love'],
			'latitude'			=> $row['latitude'],
			'longitude'			=> $row['longitude']
		));
	}

	while($row = mysqli_fetch_array($query3)){
		array_push($result2,array(
			'user_id'	=> $row['user_id']
		));
	}
	
	echo json_encode(array('result1'=>$result1, 'result2'=>$result2));
	mysqli_close($conn);

 ?>
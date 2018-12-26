<?php 

	require_once('connection.php');

	$query1 = mysqli_query($conn, "SELECT * FROM content where latitude not in('')");
	$result1 = array();
	while($row = mysqli_fetch_array($query1)){
		array_push($result1,array(
			'content_id'		=> $row['content_id'],
			'author'		=> $row['author'],
			'title'			=> $row['title'],
			'latitude'		=> $row['latitude'],
			'longitude'		=> $row['longitude']
		));
	}

	$query2 = mysqli_query($conn, "SELECT MIN( latitude ) AS min, MAX( longitude ) AS max FROM content where latitude not in('')");
	$result2 = array();
	while($row = mysqli_fetch_array($query2)){
		array_push($result2,array(
			'min'		=> $row['min'],
			'max'		=> $row['max']
		));
	}

	echo json_encode(array('result1'=>$result1, 'result2'=>$result2));
	mysqli_close($conn);

?>
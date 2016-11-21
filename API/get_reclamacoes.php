<?php

include("./connection.php");
include("./functions.php");

$params = $_REQUEST;
$error = true;
$message = "Unknow error";
$response = array();

		$query = "SELECT * 
					FROM `matheusn_reclameonibus`.`reclamacoes`";

		//echo $query;

		$result = mysql_query ($query);

		if(!$result){
			if(mysql_errno($conn)) {
				$message = "Erro ".mysql_errno($conn);
			}
			$response = null;
		}
		else{
			$error = false;
			if(mysql_num_rows($result)==0){
				$message = "Nenhuma reclamacao foi encontrada.";
				$response = null;
			}
			else{
				$message = "Reclamacoes encontradas com sucesso!";

			}
		}


echo json_encode(
	array(
		"error"=>$error,
		"message"=>$message,
		"response"=>$response
	)
);

?>
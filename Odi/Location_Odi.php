<?php
    $db_user ="root";    //추가한 계정이름(사용자명)
    $db_pass ="";     //비밀번호
    $db_host ="localhost";  
    $db_name ="probono";
    
    $con = mysqli_connect($db_host, $db_user, $db_pass, $db_name);

    $locationX = $_POST["locationX"];
    $locationY = $_POST["locationY"];
    $type = $_POST["type"];
    $userID = $_POST["userID"];

    $statement = mysqli_prepare($con, "insert into obstacle values(?,?,?,?)");
    mysqli_stmt_bind_param($statement, "ssss", $locationX, $locationY, $type, $userID);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);
?>


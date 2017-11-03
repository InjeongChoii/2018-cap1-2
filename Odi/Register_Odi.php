<?php
    $db_user ="root";    //추가한 계정이름(사용자명)
    $db_pass ="";     //비밀번호
    $db_host ="localhost";  
    $db_name ="probono";
    
    $con = mysqli_connect($db_host, $db_user, $db_pass, $db_name);

    $userID = $_POST["userID"];
    $userName = $_POST["userName"];
    $dORv = $_POST["dORv"];
    $carer = $_POST["carer"];

    $statement = mysqli_prepare($con, "insert into user values(?,?,?,?)");
    mysqli_stmt_bind_param($statement, "ssss", $userID, $userName, $dORv, $carer);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);
    


?>
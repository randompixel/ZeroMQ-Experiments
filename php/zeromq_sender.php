<?php
/**
 * Message Sender Program
 * Iterates directory looking for files, then appends php to them and sends them to Java box
 */ 

define('OUTBOX', '/home/dave/Desktop/zeromq/php/outbox/');

$context = new ZMQContext();

$sender = new ZMQSocket($context, ZMQ::SOCKET_PUSH);
$sender->bind('tcp://*:5557');

while (true) {

    $filenames = array();
    $iterator = new DirectoryIterator(OUTBOX);

    $count = 0;
    foreach ($iterator as $fileinfo) {
        if ($fileinfo->isFile()) {
            echo 'Processing ' . $fileinfo->getFilename();

            $message = file_get_contents($fileinfo->getPathname());

            $message = $fileinfo->getFilename() . "\n" . $message;

            try {
                $sender->send($message);
                echo " (SENT)\n";
                unlink($fileinfo->getPathname());
            }
            catch (ZMQSocketException $e) {
                echo "\nSocket Exception: " . $e;
            }
            $count++;  
        }
    }

    if ($count === 0) {
        echo "Nothing to do. Sleeping.\n";
        sleep(1);
    }

    
}


?>

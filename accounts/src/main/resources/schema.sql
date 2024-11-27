create table if not exists `customer` (
    `customer_id` int AUTO_INCREMENT PRIMARY KEY,
    name varchar(100) not null,
    `email` varchar(100) not null,
    `mobile_number` varchar(20) not null,
    `created_at` date not null,
    `created_by` varchar(20) not null,
    `updated_at` date default null,
    `updated_by` varchar(20) default null
);

create table if not exists `accounts` (
    `account_number` int AUTO_INCREMENT PRIMARY KEY,
    `customer_id` int not null,
    `account_type` varchar(100) not null,
    `branch_address` varchar(200) not null,
    `created_at` date not null,
    `created_by` varchar(20) not null,
    `updated_at` date default null,
    `updated_by` varchar(20) default null
);
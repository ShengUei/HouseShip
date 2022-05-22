CREATE TABLE member (
  account varchar(100) PRIMARY KEY NOT NULL,
  hashed_pwd varchar(100) NOT NULL,
  salt varchar(100) NOT NULL,
  lastname varchar(100) NOT NULL,
  firstname varchar(100) NOT NULL,
  birthday timestamp NOT NULL,
  email varchar(100) NOT NULL,
  m_address varchar(100),
  phone varchar(10) NOT NULL,
  mempic varchar(100)
);

CREATE TABLE houseinfo (
  houseNo int PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
  account varchar(100) NOT NULL,
  h_title varchar(100),
  h_address varchar(100),
  h_type int,
  h_about varchar(300),
  h_price decimal,
  offersNo int NOT NULL,
  rulesNo int NOT NULL
);

CREATE TABLE houseOffers (
  offersNo int PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
  wifi boolean,
  tv boolean,
  kitchen boolean,
  refrigerator boolean,
  microwave boolean,
  aircon boolean,
  washer boolean
);

CREATE TABLE houseRules (
  rulesNo int PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
  check_in time(0),
  check_out time(0),
  smoking boolean,
  pet boolean
);

CREATE TABLE housePhotos (
  photoNo int PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
  houseNo int,
  photoPath varchar(255)
);

CREATE TABLE orderinfo (
  orderid int PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
  account varchar(100) NOT NULL,
  orderdate timestamp NOT NULL
);

CREATE TABLE orderItem (
  itemid int PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
  orderid int NOT NULL,
  houseno int NOT NULL,
  chkinDate date NOT NULL,
  chkoutDate date NOT NULL,
  vid int
);

CREATE TABLE forum (
  fid int PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
  account varchar(100) NOT NULL,
  postTime timestamp,
  theme varchar(100),
  title varchar(100),
  content varchar(300)
);

CREATE TABLE vouchers (
  vid int PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
  v_code varchar(100) NOT NULL,
  v_title varchar(100) NOT NULL,
  v_status boolean NOT NULL,
  discount decimal NOT NULL,
  stardate timestamp NOT NULL,
  enddate timestamp NOT NULL
);

ALTER TABLE houseinfo ADD FOREIGN KEY (account) REFERENCES member (account);

ALTER TABLE houseinfo ADD FOREIGN KEY (offersNo) REFERENCES houseOffers (offersNo);

ALTER TABLE houseinfo ADD FOREIGN KEY (rulesNo) REFERENCES houseRules (rulesNo);

ALTER TABLE housePhotos ADD FOREIGN KEY (houseNo) REFERENCES houseinfo (houseNo);

ALTER TABLE orderinfo ADD FOREIGN KEY (account) REFERENCES member (account);

ALTER TABLE orderItem ADD FOREIGN KEY (orderid) REFERENCES orderinfo (orderid);

ALTER TABLE orderItem ADD FOREIGN KEY (houseno) REFERENCES houseinfo (houseNo);

ALTER TABLE orderItem ADD UNIQUE (orderid, houseno);

ALTER TABLE forum ADD FOREIGN KEY (account) REFERENCES member (account);


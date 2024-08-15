-- insert roles
INSERT INTO roles (id,name)
    VALUES (1,'ROLE_SUPER_ADMIN'),
           (2,'ROLE_ADMIN'),
           (3,'ROLE_ATHLETE');

-- insert admins
INSERT INTO users(id,firstname,lastname,email,password,is_enabled,phone_number,created_at,role_id)
    VALUES (1,'Walid','Alioua','walid@starting-cn.tn','$2a$10$CZlHZFK7QeA7Z0OtID91jORU4kf49ZhzhFmN9O.iII0Uu0gNNMnS2',true,'93300785',NOW(),1) ;

INSERT INTO admins (id)
    VALUES (1);
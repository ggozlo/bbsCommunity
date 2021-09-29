
CREATE TABLE "Member" (
    "Id" int   NOT NULL,
    "LoginId" varchar(20)   NOT NULL,
    "Password" varchar(18)   NOT NULL,
    "Email" varchar(320)   NOT NULL,
    "Name" varchar(20)   NOT NULL,
    CONSTRAINT "pk_Member" PRIMARY KEY (
        "Id"
     ),
    CONSTRAINT "uc_Member_LoginId" UNIQUE (
        "LoginId"
    ),
    CONSTRAINT "uc_Member_Email" UNIQUE (
        "Email"
    ),
    CONSTRAINT "uc_Member_Name" UNIQUE (
        "Name"
    )
);

CREATE TABLE "Post" (
    "Id" int   NOT NULL,
    "MemberId" int   NOT NULL,
    "BoardName" varchar(30)   NOT NULL,
    "Title" varchar(50)   NOT NULL,
    "Content" text   NOT NULL,
    "Suggestion" int   NOT NULL,
    "View" int   NOT NULL,
    CONSTRAINT "pk_Post" PRIMARY KEY (
        "Id"
     )
);

CREATE TABLE "Board" (
    "Name" varchar(30)   NOT NULL,
    "NickName" varchar(50)   NOT NULL,
    "Activation" bool   NOT NULL,
    "Describe" varchar(255)   NULL,
    CONSTRAINT "pk_Board" PRIMARY KEY (
        "Name"
     ),
    CONSTRAINT "uc_Board_NickName" UNIQUE (
        "NickName"
    )
);

CREATE TABLE "Comment" (
    "Id" int   NOT NULL,
    "MemberId" int   NOT NULL,
    "PostId" int   NOT NULL,
    "Content" text   NOT NULL,
    CONSTRAINT "pk_Comment" PRIMARY KEY (
        "Id"
     )
);

CREATE TABLE "Authority" (
    "MemberId" int   NOT NULL,
    "BoardName" varchar(30)   NULL,
    "Role" varchar(50)   NOT NULL
);

ALTER TABLE "Post" ADD CONSTRAINT "fk_Post_MemberId" FOREIGN KEY("MemberId")
REFERENCES "Member" ("Id");

ALTER TABLE "Post" ADD CONSTRAINT "fk_Post_BoardName" FOREIGN KEY("BoardName")
REFERENCES "Board" ("Name");

ALTER TABLE "Comment" ADD CONSTRAINT "fk_Comment_MemberId" FOREIGN KEY("MemberId")
REFERENCES "Member" ("Id");

ALTER TABLE "Comment" ADD CONSTRAINT "fk_Comment_PostId" FOREIGN KEY("PostId")
REFERENCES "Post" ("Id");

ALTER TABLE "Authority" ADD CONSTRAINT "fk_Authority_MemberId" FOREIGN KEY("MemberId")
REFERENCES "Member" ("Id");

ALTER TABLE "Authority" ADD CONSTRAINT "fk_Authority_BoardName" FOREIGN KEY("BoardName")
REFERENCES "Board" ("Name");


CREATE DATABASE CineApp
GO
USE CineApp
GO

CREATE TABLE Actor
(
	IDActor INT PRIMARY KEY IDENTITY,
	FirstName NVARCHAR(50),
	MiddleName NVARCHAR(50),
	LastName NVARCHAR(50)
)

GO

CREATE TABLE Director
(
	IDDirector INT PRIMARY KEY IDENTITY,
	FirstName NVARCHAR(50),
	MiddleName NVARCHAR(50),
	LastName NVARCHAR(50)
)

GO

CREATE TABLE Movie
(
	IDMovie INT PRIMARY KEY IDENTITY,
	Title NVARCHAR(300),
	PublishedDate NVARCHAR(100),
	Description NVARCHAR(900),
	OriginalTitle NVARCHAR(300),
	Duration INT,
	Genre NVARCHAR(100),
	PicturePath NVARCHAR(100),
	Link NVARCHAR(300),
	StartDate NVARCHAR(100)
)
GO

CREATE TABLE MovieDirector
(
	MovieID INT FOREIGN KEY REFERENCES Movie(IDMovie),
	DirectorID INT FOREIGN KEY REFERENCES Director(IDDirector)
)

GO

CREATE TABLE MovieActor
(
	MovieID INT FOREIGN KEY REFERENCES Movie(IDMovie),
	ActorID INT FOREIGN KEY REFERENCES Actor(IDActor)
)

GO

CREATE TABLE Users
(
	UserID INT PRIMARY KEY IDENTITY,
	Username NVARCHAR(50),
	Password NVARCHAR(50),
	IsAdministrator BIT
)
GO

CREATE PROCEDURE createMovie
	@Title NVARCHAR(300),
	@PublishedDate NVARCHAR(100),
	@Description NVARCHAR(900),
	@OriginalTitle NVARCHAR(300),
	@Duration INT,
	@Genre NVARCHAR(100),
	@PicturePath NVARCHAR(100),
	@Link NVARCHAR(300),
	@StartDate NVARCHAR(100),
	@Id INT OUTPUT
AS 
BEGIN
	IF EXISTS (SELECT * FROM Movie WHERE Title = @Title)
		SELECT @Id = IDMovie from Movie WHERE Title = @Title
	ELSE 
		BEGIN 
			INSERT INTO Movie VALUES(@Title, @PublishedDate, @Description, @OriginalTitle, @Duration, @Genre, @PicturePath, @Link, @StartDate)
			SET @Id = SCOPE_IDENTITY()
		END
END
GO

CREATE PROCEDURE createDirector
	@FirstName NVARCHAR(50),
	@MiddleName NVARCHAR(50),
	@LastName NVARCHAR(50),
	@Id INT OUTPUT
AS 
BEGIN
	IF EXISTS (SELECT *FROM Director WHERE 
		(FirstName = @FirstName OR (ISNULL(FirstName, @FirstName) IS NULL)) AND 
		(MiddleName = @MiddleName OR (ISNULL(MiddleName, @MiddleName) IS NULL)) AND
		(LastName = @LastName OR (ISNULL(LastName, @LastName) IS NULL)))
		BEGIN
			SELECT @Id = IDDirector from Director WHERE 
			(FirstName = @FirstName OR (ISNULL(FirstName,@FirstName) IS NULL)) AND 
			(MiddleName = @MiddleName OR (ISNULL(MiddleName,@MiddleName) IS NULL)) AND
			(LastName = @LastName OR (ISNULL(LastName,@LastName) IS NULL))
		END
	ELSE
		BEGIN 
			INSERT INTO Director VALUES(@FirstName, @MiddleName, @LastName)
			SET @Id = SCOPE_IDENTITY()
		END
END
GO

CREATE PROCEDURE createMovieDirector
	@MovieID int,
	@DirectorID int
AS
BEGIN
	IF NOT EXISTS(SELECT *from MovieDirector WHERE MovieID = @MovieID AND DirectorID = @DirectorID)
		BEGIN
			INSERT INTO MovieDirector VALUES (@MovieID, @DirectorID)
		END
END
GO

CREATE PROCEDURE createActor
	@FirstName NVARCHAR(50),
	@MiddleName NVARCHAR(50),
	@LastName NVARCHAR(50),
	@Inserted INT OUTPUT,
	@Id INT OUTPUT
AS 
BEGIN
	IF EXISTS (SELECT *FROM Actor WHERE 
		(FirstName = @FirstName OR (ISNULL(FirstName, @FirstName) IS NULL)) AND 
		(MiddleName = @MiddleName OR (ISNULL(MiddleName, @MiddleName) IS NULL)) AND
		(LastName = @LastName OR (ISNULL(LastName, @LastName) IS NULL)))
		BEGIN
			SELECT @Id = IDActor from Actor WHERE 
			(FirstName = @FirstName OR (ISNULL(FirstName,@FirstName) IS NULL)) AND 
			(MiddleName = @MiddleName OR (ISNULL(MiddleName,@MiddleName) IS NULL)) AND
			(LastName = @LastName OR (ISNULL(LastName,@LastName) IS NULL))
			SET @Inserted = 0
		END

	ELSE
		BEGIN 
			INSERT INTO Actor VALUES(@FirstName, @MiddleName, @LastName)
			SET @Id = SCOPE_IDENTITY()
			SET @Inserted = 1
		END
END
GO

CREATE PROCEDURE createMovieActor
	@MovieID int,
	@ActorID int
AS
BEGIN
	IF NOT EXISTS(SELECT *from MovieActor WHERE MovieID = @MovieID AND ActorID = @ActorID)
		BEGIN
			INSERT INTO MovieActor VALUES (@MovieID, @ActorID)
		END
END
GO

CREATE PROCEDURE updateMovie
	@Title NVARCHAR(300),
	@PublishedDate NVARCHAR(100),
	@Description NVARCHAR(900),
	@OriginalTitle NVARCHAR(300),
	@Duration INT,
	@Genre NVARCHAR(100),
	@PicturePath NVARCHAR(100),
	@Link NVARCHAR(300),
	@StartDate NVARCHAR(100),
	@IDMovie INT
AS 
BEGIN 
	UPDATE Movie SET 
		Title = @Title,
		PublishedDate = @PublishedDate,
		Description = @Description,
		OriginalTitle = @OriginalTitle,
		Duration = @Duration,
		Genre = @Genre,
		PicturePath = @PicturePath,
		Link = @Link,
		StartDate = @StartDate
	WHERE 
		IDMovie = @IDMovie
END
GO

CREATE PROCEDURE deleteMovie
	@IDMovie INT	 
AS 
BEGIN 
	DELETE FROM MovieDirector 
	WHERE MovieID = @IDMovie

	DELETE FROM MovieActor
	WHERE MovieID = @IDMovie

	DELETE FROM Movie
	WHERE IDMovie = @IDMovie
END
GO

CREATE PROCEDURE deleteAllData
AS 
BEGIN 
	DELETE FROM MovieDirector 

	DELETE FROM MovieActor

	DELETE FROM Movie
	DBCC CHECKIDENT(Movie, RESEED, 0);

	DELETE FROM Actor
	DBCC CHECKIDENT(Actor, RESEED, 0);

	DELETE FROM Director
	DBCC CHECKIDENT(Director, RESEED, 0);
END
GO

CREATE PROCEDURE deleteMoviePeople
	@IDMovie INT
AS 
BEGIN
	DELETE FROM MovieDirector
	WHERE MovieID = @IDMovie

	DELETE FROM MovieActor
	WHERE MovieID = @IDMovie
END
GO

CREATE PROCEDURE selectMovie
	@IDMovie INT
AS 
BEGIN 
	SELECT *FROM Movie 
	WHERE IDMovie = @IDMovie
END
GO

CREATE PROCEDURE selectMovieDirectors
	@IDMovie INT
AS 
BEGIN 
	SELECT Director.* FROM Movie
	INNER JOIN MovieDirector
	ON MovieID = IDMovie
	INNER JOIN Director
	ON DirectorID = IDDirector
	WHERE IDMovie = @IDMovie
END
GO

CREATE PROCEDURE selectMovieActors
	@IDMovie INT
AS 
BEGIN 
	SELECT Actor.* FROM Movie
	INNER JOIN MovieActor
	ON MovieID = IDMovie
	INNER JOIN Actor
	ON ActorID = IDActor
	WHERE IDMovie = @IDMovie
END
GO

CREATE PROCEDURE selectMovies
AS 
BEGIN 
	SELECT IDMovie FROM MOVIE
END
GO

CREATE PROCEDURE selectActors
AS
BEGIN
	SELECT *FROM Actor
END
GO

CREATE PROCEDURE createAdminUser
AS
BEGIN
	IF NOT EXISTS(SELECT *from Users WHERE Username = 'admin')
	BEGIN
		INSERT INTO Users VALUES('admin','admin', 1)
	END
END
GO

CREATE PROCEDURE createUser
	@Username NVARCHAR(50),
	@Password NVARCHAR(50),
	@Inserted BIT OUTPUT 
AS
BEGIN
	IF NOT EXISTS(SELECT *from Users WHERE Username = @Username)
	BEGIN
		INSERT INTO Users VALUES(@Username,@Password, 0)
	END
	SET @Inserted = @@ROWCOUNT
END
GO

CREATE PROCEDURE selectUser
	@Username NVARCHAR(50),
	@Password NVARCHAR(50)
AS
BEGIN
	SELECT *FROM Users WHERE Username = @Username AND Password = @Password	
END
GO


	
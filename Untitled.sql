CREATE TABLE [Role] (
  [id] integer,
  [role] integer
)
GO

CREATE TABLE [UsersRoles] (
  [id] integer,
  [role_id] integer,
  [user_id] integer
)
GO

CREATE TABLE [Users] (
  [id] integer PRIMARY KEY,
  [username] nvarchar(255),
  [password] nvarchar(255),
  [role_id] nvarchar(255),
  [created_at] timestamp
)
GO

CREATE TABLE [Posts] (
  [id] integer PRIMARY KEY,
  [title] nvarchar(255),
  [body] text,
  [user_id] integer,
  [status] nvarchar(255),
  [created_at] timestamp
)
GO

EXEC sp_addextendedproperty
@name = N'Column_Description',
@value = 'Content of the post',
@level0type = N'Schema', @level0name = 'dbo',
@level1type = N'Table',  @level1name = 'Posts',
@level2type = N'Column', @level2name = 'body';
GO

ALTER TABLE [Posts] ADD FOREIGN KEY ([user_id]) REFERENCES [Users] ([id])
GO

ALTER TABLE [UsersRoles] ADD FOREIGN KEY ([user_id]) REFERENCES [Users] ([id])
GO

ALTER TABLE [UsersRoles] ADD FOREIGN KEY ([role_id]) REFERENCES [Role] ([id])
GO

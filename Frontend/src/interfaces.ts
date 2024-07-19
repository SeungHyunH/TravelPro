// interfaces.ts

export interface UserDto {
  username: string;
  password: string;
}

export interface UserEntity {
  id: number;
  username: string;
  password: string;
}

export interface UserResult {
  id: number;
  username: string;
  password: string;
}

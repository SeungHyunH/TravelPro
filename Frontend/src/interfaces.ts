// interfaces.ts

// User Data Transfer Object (DTO)
export interface UserDto {
    name: string;
    email: string;
  }
  
  // User Entity
  export interface UserEntity {
    id: number;
    name: string;
    email: string;
  }
  
  // API Response containing the user entity
  export interface UserResult {
    user: UserEntity;
  }
  
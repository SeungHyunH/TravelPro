'use client'; // 클라이언트 측에서 실행되어야 하는 컴포넌트에서 선언

import { useState } from 'react';
import { UserDto, UserEntity, UserResult } from '../interfaces';

export default function Home() {
  const [user, setUser] = useState<UserEntity | null>(null);
  const [userId, setUserId] = useState<string>('');
  const [name, setName] = useState<string>('');
  const [email, setEmail] = useState<string>('');

  const createUser = async () => {
    const response = await fetch('/api', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ name, email } as UserDto),
    });

    if (response.ok) {
      const result: UserResult = await response.json();
      setUser(result.user);
    } else {
      console.error('Failed to create user');
    }
  };

  const getUser = async () => {
    const response = await fetch(`/api/${userId}`);
    if (response.ok) {
      const result: UserResult = await response.json();
      setUser(result.user);
    } else {
      console.error('Failed to fetch user');
    }
  };

  return (
    <div>
      <h1>Create User</h1>
      <input
        type="text"
        placeholder="Name"
        value={name}
        onChange={(e) => setName(e.target.value)}
      />
      <input
        type="email"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />
      <button onClick={createUser}>Create User</button>

      <h1>Get User</h1>
      <input
        type="text"
        placeholder="User ID"
        value={userId}
        onChange={(e) => setUserId(e.target.value)}
      />
      <button onClick={getUser}>Get User</button>

      {user && (
        <div>
          <h2>User Details</h2>
          <p>ID: {user.id}</p>
          <p>Name: {user.name}</p>
          <p>Email: {user.email}</p>
        </div>
      )}
    </div>
  );
}

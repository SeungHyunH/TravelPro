"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

const Register = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");
  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const response = await fetch("/api/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email, password, name }),
    });

    if (response.ok) {
      console.log(response);
    } else {
      alert("Registration failed");
    }
  };

  return (
    <>
      <h1>Register</h1>
      <form onSubmit={handleSubmit}>
        <label htmlFor="name">Name:</label>
        <input
          type="text"
          id="name"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
        />
        <label htmlFor="email">Email:</label>
        <input
          type="email"
          id="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <label htmlFor="password">Password:</label>
        <input
          type="password"
          id="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit">Register</button>
      </form>
      <div>
        <a href="/api/auth/google?redirect_uri=http://localhost/auth/redirect">
          <button>Register with Google</button>
        </a>
        <a href="/api/auth/kakao?redirect_uri=http://localhost/auth/redirect">
          <button>Register with Kakao</button>
        </a>
        <a href="/api/auth/naver?redirect_uri=http://localhost/auth/redirect">
          <button>Register with Naver</button>
        </a>
      </div>
      <a href="/">홈</a>
      <a href="/auth/login">로그인</a>
    </>
  );
};

export default Register;

"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import axios from "axios";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await axios.post("/api/user/login", {
        email,
        password,
      });

      if (response.status === 200) {
        // 로그인 성공시 처리
        console.log(response.data);
        // 홈으로 리디렉션
        router.push("/");
      } else {
        alert("Login failed");
      }
    } catch (error) {
      console.error("There was an error!", error);
      alert("Login failed");
    }
  };

  return (
    <>
      <h1>Login</h1>
      <form onSubmit={handleSubmit}>
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
        <button type="submit">Login</button>
      </form>
      <div>
        <a href="/api/auth/social/google">
          <button>Register with Google</button>
        </a>
        <a href="/api/auth/social/kakao">
          <button>Register with Kakao</button>
        </a>
        <a href="/api/auth/social/naver">
          <button>Register with Naver</button>
        </a>
      </div>
      <a href="/">홈</a>
      <a href="/auth/register">회원가입</a>
    </>
  );
};

export default Login;

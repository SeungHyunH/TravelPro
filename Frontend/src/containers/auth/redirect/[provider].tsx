"use client";

import { usePathname, useRouter, useSearchParams } from "next/navigation";
import { useEffect } from "react";
import axios from "axios";

const RedirectPage = () => {
  const pathname = usePathname();
  const searchParams = useSearchParams();
  const router = useRouter();

  useEffect(() => {
    const code = searchParams.get("code");
    const provider = pathname.split("/")[3];

    if (provider && code) {
      axios
        .post("/api/auth/oauth2/callback", { code, provider })
        .then((response) => {
          console.log(response);
        })
        .catch((error) => {
          const code = error.response.data.code;
          const data = error.response.data.message;
          if (code == -401) {
            localStorage.setItem("token", data);
            router.replace("/");
          }
          console.log(code, data);
        });
    }
  }, [pathname, searchParams]);

  return <div>Processing...</div>;
};

export default RedirectPage;

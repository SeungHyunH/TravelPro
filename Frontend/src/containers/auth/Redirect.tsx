import { useRouter } from "next/navigation";
import { useEffect } from "react";
import axios from "axios";

const RedirectPage = () => {
  const router = useRouter();
  const { searchParams } = new URL(window.location.href);

  useEffect(() => {
    const code = searchParams.get("code");
    const provider = searchParams.get("provider");

    if (code && provider) {
      axios
        .post("/api/auth/callback", { code, provider })
        .then((response) => {
          console.log(response);
          // router.push("/");
        })
        .catch((error) => {
          console.error("Login failed", error);
          router.push("/error");
        });
    }
  }, [searchParams, router]);

  return (
    <>
      <h1>Processing...</h1>
    </>
  );
};

export default RedirectPage;

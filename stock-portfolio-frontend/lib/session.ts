import type { IronSessionOptions } from "iron-session";
import type { User } from "../pages/api/user";
export const sessionOptions: IronSessionOptions = {
  cookieName: "cookiemonster",
  password: process.env.SECRET_COOKIE_PASSWORD,
  // secure: true should be used in production (HTTPS) but can't be used in development (HTTP)
  cookieOptions: {
    secure: process.env.NODE_ENV === "production",
  },
};

declare module "iron-session" {
  interface IronSessionData {
    user?: User;
  }
}

import type { IronSessionOptions } from "iron-session";
import { withIronSessionApiRoute } from "iron-session/next";
import { NextApiHandler, NextApiRequest, NextApiResponse } from "next";
import type { User } from "../pages/api/user";

export const sessionOptions: IronSessionOptions = {
  cookieName: "cookiemonster",
  password: process.env.SECRET_COOKIE_PASSWORD,
  // secure: true should be used in production (HTTPS) but can't be used in development (HTTP)
  cookieOptions: {
    secure: process.env.NODE_ENV === "production",
  },
};

export function withSessionRoute(handler: NextApiHandler) {
  return withIronSessionApiRoute(handler, sessionOptions);
}

type UserNextApiHandler<T = any> = (
  req: NextApiRequest,
  res: NextApiResponse<T>,
  user?: User
) => Promise<unknown> & unknown;

export function withUserSessionRoute(handler: UserNextApiHandler) {
  return withIronSessionApiRoute(async (req, res) => {
    let user = req.session.user;
    if (user == undefined || user.userid == undefined) {
      res.status(403).json({ error: "No user session" });
    } else {
      console.log("user details:", user);
      await handler(req, res, user);
    }
  }, sessionOptions);
}
declare module "iron-session" {
  interface IronSessionData {
    user?: User;
  }
}

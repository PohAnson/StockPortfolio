import { withIronSessionApiRoute } from "iron-session/next";
import { sessionOptions } from "../../lib/session";
export interface User {
  isLogin: boolean;
  userid: string;
  name: string;
}

export default withIronSessionApiRoute(function userRoute(req, res) {
  if (req.session.user) {
    res.json({ ...req.session.user, isLogin: true });
  } else {
    res.json({ isLogin: false });
  }
}, sessionOptions);

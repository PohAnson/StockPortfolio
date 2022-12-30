import { withSessionRoute } from "../../lib/session";
export interface User {
  isLogin: boolean;
  userid: string;
  name: string;
}

export default withSessionRoute(function userRoute(req, res) {
  if (req.session.user) {
    res.json({ ...req.session.user, isLogin: true });
  } else {
    res.json({ isLogin: false });
  }
});

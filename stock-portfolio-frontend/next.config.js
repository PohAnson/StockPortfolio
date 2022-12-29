/** @type {import('next').NextConfig} */

module.exports = {
  reactStrictMode: true,
  swcMinify: true,
  webpack: (config, { dev }) => {
    if (dev) {
      config.watchOptions = {
        poll: true,
        ignored: "/node_modules",
      };
    }
    return config;
  },
};
